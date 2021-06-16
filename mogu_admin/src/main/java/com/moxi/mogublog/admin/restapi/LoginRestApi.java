package com.moxi.mogublog.admin.restapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.admin.global.MessageConf;
import com.moxi.mogublog.admin.global.RedisConf;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.commons.config.jwt.Audience;
import com.moxi.mogublog.commons.config.jwt.JwtTokenUtil;
import com.moxi.mogublog.commons.entity.Admin;
import com.moxi.mogublog.commons.entity.CategoryMenu;
import com.moxi.mogublog.commons.entity.OnlineAdmin;
import com.moxi.mogublog.commons.entity.Role;
import com.moxi.mogublog.commons.feign.PictureFeignClient;
import com.moxi.mogublog.utils.*;
import com.moxi.mogublog.xo.service.AdminService;
import com.moxi.mogublog.xo.service.CategoryMenuService;
import com.moxi.mogublog.xo.service.RoleService;
import com.moxi.mogublog.xo.service.WebConfigService;
import com.moxi.mogublog.xo.utils.WebUtil;
import com.moxi.mougblog.base.enums.EMenuType;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 登录管理 RestApi【为了更好地使用security放行把登录管理放在AuthRestApi中】
 *
 * @author limbo
 * @date 2018-10-14
 */
@RestController
@RefreshScope
@RequestMapping("/auth")
@Api(value = "登录相关接口", tags = {"登录相关接口"})
@Slf4j
public class LoginRestApi {

    @Autowired
    private WebUtil webUtil;
    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private CategoryMenuService categoryMenuService;
    @Autowired
    private Audience audience;
    @Value(value = "${tokenHead}")
    private String tokenHead;
    @Value(value = "${isRememberMeExpiresSecond}")
    private int isRememberMeExpiresSecond;
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private PictureFeignClient pictureFeignClient;
    @Autowired
    private WebConfigService webConfigService;

    @ApiOperation(value = "用户登录", notes = "用户登录")
    @PostMapping("/login")
    public String login(HttpServletRequest request,
                        @ApiParam(name = "username", value = "用户名或邮箱或手机号") @RequestParam(name = "username", required = false) String username,
                        @ApiParam(name = "password", value = "密码") @RequestParam(name = "password", required = false) String password,
                        @ApiParam(name = "isRememberMe", value = "是否记住账号密码") @RequestParam(name = "isRememberMe", required = false, defaultValue = "false") Boolean isRememberMe) {

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ResultUtil.result(SysConf.ERROR, "账号或密码不能为空");
        }
        String ip = IpUtils.getIpAddr(request);
        String limitCount = redisUtil.get(RedisConf.LOGIN_LIMIT + RedisConf.SEGMENTATION + ip);
        if (StringUtils.isNotEmpty(limitCount)) {
            Integer tempLimitCount = Integer.valueOf(limitCount);
            if (tempLimitCount >= Constants.NUM_FIVE) {
                return ResultUtil.result(SysConf.ERROR, "密码输错次数过多,已被锁定30分钟");
            }
        }
        Boolean isEmail = CheckUtils.checkEmail(username);
        Boolean isMobile = CheckUtils.checkMobileNumber(username);
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        if (isEmail) {
            queryWrapper.eq(SQLConf.EMAIL, username);
        } else if (isMobile) {
            queryWrapper.eq(SQLConf.MOBILE, username);
        } else {
            queryWrapper.eq(SQLConf.USER_NAME, username);
        }
        queryWrapper.last(SysConf.LIMIT_ONE);
        queryWrapper.eq(SysConf.STATUS, EStatus.ENABLE);
        Admin admin = adminService.getOne(queryWrapper);
        if (admin == null) {
            // 设置错误登录次数
            log.error("该管理员不存在");
            return ResultUtil.result(SysConf.ERROR, String.format(MessageConf.LOGIN_ERROR, setLoginCommit(request)));
        }
        // 对密码进行加盐加密验证，采用SHA-256 + 随机盐【动态加盐】 + 密钥对密码进行加密
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean isPassword = encoder.matches(password, admin.getPassWord());
        if (!isPassword) {
            //密码错误，返回提示
            log.error("管理员密码错误");
            return ResultUtil.result(SysConf.ERROR, String.format(MessageConf.LOGIN_ERROR, setLoginCommit(request)));
        }
        List<String> roleUids = new ArrayList<>();
        roleUids.add(admin.getRoleUid());
        List<Role> roles = (List<Role>) roleService.listByIds(roleUids);

        if (roles.size() <= 0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.NO_ROLE);
        }
        String roleNames = null;
        for (Role role : roles) {
            roleNames += (role.getRoleName() + Constants.SYMBOL_COMMA);
        }
        String roleName = roleNames.substring(0, roleNames.length() - 2);
        long expiration = isRememberMe ? isRememberMeExpiresSecond : audience.getExpiresSecond();
        String jwtToken = jwtTokenUtil.createJWT(admin.getUserName(),
                admin.getUid(),
                roleName,
                audience.getClientId(),
                audience.getName(),
                expiration * 1000,
                audience.getBase64Secret());
        String token = tokenHead + jwtToken;
        Map<String, Object> result = new HashMap<>(Constants.NUM_ONE);
        result.put(SysConf.TOKEN, token);

        //进行登录相关操作
        Integer count = admin.getLoginCount() + 1;
        admin.setLoginCount(count);
        admin.setLastLoginIp(IpUtils.getIpAddr(request));
        admin.setLastLoginTime(new Date());
        admin.updateById();
        // 设置token到validCode，用于记录登录用户
        admin.setValidCode(token);
        // 设置tokenUid，【主要用于换取token令牌，防止token直接暴露到在线用户管理中】
        admin.setTokenUid(StringUtils.getUUID());
        admin.setRole(roles.get(0));
        // 添加在线用户到Redis中【设置过期时间】
        adminService.addOnlineAdmin(admin, expiration);
        return ResultUtil.result(SysConf.SUCCESS, result);
    }

    @ApiOperation(value = "用户信息", notes = "用户信息", response = String.class)
    @GetMapping(value = "/info")
    public String info(HttpServletRequest request,
                       @ApiParam(name = "token", value = "token令牌", required = false) @RequestParam(name = "token", required = false) String token) {

        Map<String, Object> map = new HashMap<>(Constants.NUM_THREE);
        if (request.getAttribute(SysConf.ADMIN_UID) == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }
        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());
        map.put(SysConf.TOKEN, token);
        //获取图片
        if (StringUtils.isNotEmpty(admin.getAvatar())) {
            String pictureList = this.pictureFeignClient.getPicture(admin.getAvatar(), SysConf.FILE_SEGMENTATION);
            List<String> list = webUtil.getPicture(pictureList);
            if (list.size() > 0) {
                map.put(SysConf.AVATAR, list.get(0));
            } else {
                map.put(SysConf.AVATAR, "https://gitee.com/moxi159753/wx_picture/raw/master/picture/favicon.png");
            }
        }

        List<String> roleUid = new ArrayList<>();
        roleUid.add(admin.getRoleUid());
        Collection<Role> roleList = roleService.listByIds(roleUid);
        map.put(SysConf.ROLES, roleList);
        return ResultUtil.result(SysConf.SUCCESS, map);
    }

    @ApiOperation(value = "获取当前用户的菜单", notes = "获取当前用户的菜单", response = String.class)
    @GetMapping(value = "/getMenu")
    public String getMenu(HttpServletRequest request) {

        Collection<CategoryMenu> categoryMenuList = new ArrayList<>();
        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());

        List<String> roleUid = new ArrayList<>();
        roleUid.add(admin.getRoleUid());
        Collection<Role> roleList = roleService.listByIds(roleUid);
        List<String> categoryMenuUids = new ArrayList<>();
        roleList.forEach(item -> {
            String caetgoryMenuUids = item.getCategoryMenuUids();
            String[] uids = caetgoryMenuUids.replace("[", "").replace("]", "").replace("\"", "").split(",");
            categoryMenuUids.addAll(Arrays.asList(uids));
        });
        categoryMenuList = categoryMenuService.listByIds(categoryMenuUids);

        // 从三级级分类中查询出 二级分类
        List<CategoryMenu> buttonList = new ArrayList<>();
        Set<String> secondMenuUidList = new HashSet<>();
        categoryMenuList.forEach(item -> {
            // 查询二级分类
            if (item.getMenuType() == EMenuType.MENU && item.getMenuLevel() == SysConf.TWO) {
                secondMenuUidList.add(item.getUid());
            }
            // 从三级分类中，得到二级分类
            if (item.getMenuType() == EMenuType.BUTTON && StringUtils.isNotEmpty(item.getParentUid())) {
                // 找出二级菜单
                secondMenuUidList.add(item.getParentUid());
                // 找出全部按钮
                buttonList.add(item);
            }
        });

        Collection<CategoryMenu> childCategoryMenuList = new ArrayList<>();
        Collection<CategoryMenu> parentCategoryMenuList = new ArrayList<>();
        List<String> parentCategoryMenuUids = new ArrayList<>();

        if (secondMenuUidList.size() > 0) {
            childCategoryMenuList = categoryMenuService.listByIds(secondMenuUidList);
        }

        childCategoryMenuList.forEach(item -> {
            //选出所有的二级分类
            if (item.getMenuLevel() == SysConf.TWO) {

                if (StringUtils.isNotEmpty(item.getParentUid())) {
                    parentCategoryMenuUids.add(item.getParentUid());
                }
            }
        });

        if (parentCategoryMenuUids.size() > 0) {
            parentCategoryMenuList = categoryMenuService.listByIds(parentCategoryMenuUids);
        }

        List<CategoryMenu> list = new ArrayList<>(parentCategoryMenuList);

        //对parent进行排序
        Map<String, Object> map = new HashMap<>(Constants.NUM_THREE);
        Collections.sort(list);
        map.put(SysConf.PARENT_LIST, list);
        map.put(SysConf.SON_LIST, childCategoryMenuList);
        map.put(SysConf.BUTTON_LIST, buttonList);
        return ResultUtil.result(SysConf.SUCCESS, map);
    }

    @ApiOperation(value = "获取网站名称", notes = "获取网站名称", response = String.class)
    @GetMapping(value = "/getWebSiteName")
    public String getWebSiteName() {
        return ResultUtil.successWithData(webConfigService.getWebSiteName());
    }


    @ApiOperation(value = "退出登录", notes = "退出登录", response = String.class)
    @PostMapping(value = "/logout")
    public String logout() {
        ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attribute.getRequest();
        String token = request.getAttribute(SysConf.TOKEN).toString();
        if (StringUtils.isEmpty(token)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.OPERATION_FAIL);
        } else {
            // 获取在线用户信息
            String adminJson = redisUtil.get(RedisConf.LOGIN_TOKEN_KEY + RedisConf.SEGMENTATION + token);
            if (StringUtils.isNotEmpty(adminJson)) {
                OnlineAdmin onlineAdmin = JsonUtils.jsonToPojo(adminJson, OnlineAdmin.class);
                String tokenUid = onlineAdmin.getTokenId();
                // 移除Redis中的TokenUid
                redisUtil.delete(RedisConf.LOGIN_UUID_KEY + RedisConf.SEGMENTATION + tokenUid);
            }
            // 移除Redis中的用户
            redisUtil.delete(RedisConf.LOGIN_TOKEN_KEY + RedisConf.SEGMENTATION + token);
            SecurityContextHolder.clearContext();
            return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
        }
    }

    /**
     * 设置登录限制，返回剩余次数
     * 密码错误五次，将会锁定30分钟
     *
     * @param request
     */
    private Integer setLoginCommit(HttpServletRequest request) {
        String ip = IpUtils.getIpAddr(request);
        String count = redisUtil.get(RedisConf.LOGIN_LIMIT + RedisConf.SEGMENTATION + ip);
        Integer surplusCount = 5;
        if (StringUtils.isNotEmpty(count)) {
            Integer countTemp = Integer.valueOf(count) + 1;
            surplusCount = surplusCount - countTemp;
            redisUtil.setEx(RedisConf.LOGIN_LIMIT + RedisConf.SEGMENTATION + ip, String.valueOf(countTemp), 30, TimeUnit.MINUTES);
        } else {
            surplusCount = surplusCount - 1;
            redisUtil.setEx(RedisConf.LOGIN_LIMIT + RedisConf.SEGMENTATION + ip, Constants.STR_ONE, 30, TimeUnit.MINUTES);
        }
        return surplusCount;
    }

}
