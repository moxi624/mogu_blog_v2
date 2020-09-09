package com.moxi.mogublog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.commons.entity.Admin;
import com.moxi.mogublog.commons.entity.OnlineAdmin;
import com.moxi.mogublog.commons.entity.Role;
import com.moxi.mogublog.commons.feign.PictureFeignClient;
import com.moxi.mogublog.utils.*;
import com.moxi.mogublog.xo.global.MessageConf;
import com.moxi.mogublog.xo.global.RedisConf;
import com.moxi.mogublog.xo.global.SQLConf;
import com.moxi.mogublog.xo.global.SysConf;
import com.moxi.mogublog.xo.mapper.AdminMapper;
import com.moxi.mogublog.xo.service.AdminService;
import com.moxi.mogublog.xo.service.RoleService;
import com.moxi.mogublog.xo.service.SysParamsService;
import com.moxi.mogublog.xo.utils.WebUtil;
import com.moxi.mogublog.xo.vo.AdminVO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.exception.exceptionType.AddException;
import com.moxi.mougblog.base.exception.exceptionType.UpdateException;
import com.moxi.mougblog.base.global.BaseSysConf;
import com.moxi.mougblog.base.holder.RequestHolder;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@Service
public class AdminServiceImpl extends SuperServiceImpl<AdminMapper, Admin> implements AdminService {

    @Resource
    AdminMapper adminMapper;

    @Autowired
    WebUtil webUtil;

    @Autowired
    AdminService adminService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    SysParamsService sysParamsService;
    @Autowired
    private PictureFeignClient pictureFeignClient;
    @Autowired
    private RoleService roleService;

    @Override
    public Admin getAdminByUid(String uid) {
        return adminMapper.getAdminByUid(uid);
    }

    @Override
    public String getOnlineAdminList(AdminVO adminVO) {
        Set<String> keys = redisUtil.keys(RedisConf.LOGIN_TOKEN_KEY + "*");
        List<String> onlineAdminList = redisUtil.multiGet(keys);
        List<OnlineAdmin> onlineAdmins = new ArrayList<>();
        for (String item : onlineAdminList) {
            OnlineAdmin onlineAdmin = JsonUtils.jsonToPojo(item, OnlineAdmin.class);
            onlineAdmins.add(onlineAdmin);
        }
        return ResultUtil.result(SysConf.SUCCESS, onlineAdmins);
    }

    @Override
    public Admin getAdminByUser(String userName) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.USER_NAME, userName);
        queryWrapper.last("LIMIT 1");
        //清空密码，防止泄露
        Admin admin = adminService.getOne(queryWrapper);
        admin.setPassWord(null);
        //获取图片
        if (StringUtils.isNotEmpty(admin.getAvatar())) {
            String pictureList = this.pictureFeignClient.getPicture(admin.getAvatar(), ",");
            admin.setPhotoList(webUtil.getPicture(pictureList));
        }
        Admin result = new Admin();
        result.setNickName(admin.getNickName());
        result.setOccupation(admin.getOccupation());
        result.setSummary(admin.getSummary());
        result.setAvatar(admin.getAvatar());
        result.setPhotoList(admin.getPhotoList());
        result.setPersonResume(admin.getPersonResume());
        return result;
    }

    @Override
    public Admin getMe() {
        HttpServletRequest request = RequestHolder.getRequest();
        if (request.getAttribute(SysConf.ADMIN_UID) == null || request.getAttribute(SysConf.ADMIN_UID) == "") {
            return new Admin();
        }
        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());

        //清空密码，防止泄露
        admin.setPassWord(null);

        //获取图片
        if (StringUtils.isNotEmpty(admin.getAvatar())) {
            String pictureList = this.pictureFeignClient.getPicture(admin.getAvatar(), ",");
            admin.setPhotoList(webUtil.getPicture(pictureList));
        }
        return admin;
    }

    @Override
    public void addOnlineAdmin(Admin admin) {
        HttpServletRequest request = RequestHolder.getRequest();
        Map<String, String> map = IpUtils.getOsAndBrowserInfo(request);
        String os = map.get(SysConf.OS);
        String browser = map.get(SysConf.BROWSER);
        String ip = IpUtils.getIpAddr(request);
        OnlineAdmin onlineAdmin = new OnlineAdmin();
        onlineAdmin.setAdminUid(admin.getUid());
        onlineAdmin.setTokenId(admin.getValidCode());
        onlineAdmin.setOs(os);
        onlineAdmin.setBrowser(browser);
        onlineAdmin.setIpaddr(ip);
        onlineAdmin.setLoginTime(DateUtils.getNowTime());
        onlineAdmin.setRoleName(admin.getRole().getRoleName());
        onlineAdmin.setUserName(admin.getUserName());
        //从Redis中获取IP来源
        String jsonResult = redisUtil.get(SysConf.IP_SOURCE + BaseSysConf.REDIS_SEGMENTATION + ip);
        if (StringUtils.isEmpty(jsonResult)) {
            String addresses = IpUtils.getAddresses(SysConf.IP + SysConf.EQUAL_TO + ip, SysConf.UTF_8);
            if (StringUtils.isNotEmpty(addresses)) {
                onlineAdmin.setLoginLocation(addresses);
                redisUtil.setEx(SysConf.IP_SOURCE + BaseSysConf.REDIS_SEGMENTATION + ip, addresses, 24, TimeUnit.HOURS);
            }
        } else {
            onlineAdmin.setLoginLocation(jsonResult);
        }
        redisUtil.setEx(RedisConf.LOGIN_TOKEN_KEY + RedisConf.SEGMENTATION + admin.getValidCode(), JsonUtils.objectToJson(onlineAdmin), 30, TimeUnit.MINUTES);
    }

    @Override
    public String getList(AdminVO adminVO) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        String pictureResult = null;
        if (StringUtils.isNotEmpty(adminVO.getKeyword())) {
            queryWrapper.like(SQLConf.USER_NAME, adminVO.getKeyword()).or().like(SQLConf.NICK_NAME, adminVO.getKeyword().trim());
        }
        Page<Admin> page = new Page<>();
        page.setCurrent(adminVO.getCurrentPage());
        page.setSize(adminVO.getPageSize());
        // 去除密码
        queryWrapper.select(Admin.class, i -> !i.getProperty().equals(SQLConf.PASS_WORD));
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        IPage<Admin> pageList = adminService.page(page, queryWrapper);
        List<Admin> list = pageList.getRecords();

        final StringBuffer fileUids = new StringBuffer();
        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getAvatar())) {
                fileUids.append(item.getAvatar() + SysConf.FILE_SEGMENTATION);
            }
        });

        Map<String, String> pictureMap = new HashMap<>();

        if (fileUids != null) {
            pictureResult = this.pictureFeignClient.getPicture(fileUids.toString(), SysConf.FILE_SEGMENTATION);
        }
        List<Map<String, Object>> picList = webUtil.getPictureMap(pictureResult);

        picList.forEach(item -> {
            pictureMap.put(item.get(SQLConf.UID).toString(), item.get(SQLConf.URL).toString());
        });

        for (Admin item : list) {

            Role role = roleService.getById(item.getRoleUid());

            item.setRole(role);

            //获取图片
            if (StringUtils.isNotEmpty(item.getAvatar())) {
                List<String> pictureUidsTemp = StringUtils.changeStringToString(item.getAvatar(), SysConf.FILE_SEGMENTATION);
                List<String> pictureListTemp = new ArrayList<>();
                pictureUidsTemp.forEach(picture -> {
                    if (pictureMap.get(picture) != null && pictureMap.get(picture) != "") {
                        pictureListTemp.add(pictureMap.get(picture));
                    }
                });
                item.setPhotoList(pictureListTemp);
            }
        }
        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }

    @Override
    public String addAdmin(AdminVO adminVO) {

        String mobile = adminVO.getMobile();
        String userName = adminVO.getUserName();
        String email = adminVO.getEmail();

        if (StringUtils.isEmpty(userName)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }

        if (StringUtils.isEmpty(email) && StringUtils.isEmpty(mobile)) {
            return ResultUtil.result(SysConf.ERROR, "邮箱和手机号至少一项不能为空");
        }

        String defaultPasswordKey = RedisConf.SYSTEM_PARAMS + RedisConf.SEGMENTATION + SysConf.SYS_DEFAULT_PASSWORD;
        String defaultPassword = sysParamsService.getSysParamsValueByKey(defaultPasswordKey);
        if (StringUtils.isEmpty(defaultPassword)) {
            throw new AddException(MessageConf.PLEASE_CONFIGURE_PASSWORD);
        }

        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.USER_NAME, userName);
        Admin temp = adminService.getOne(queryWrapper);

        if (temp == null) {
            Admin admin = new Admin();
            admin.setAvatar(adminVO.getAvatar());
            admin.setEmail(adminVO.getEmail());
            admin.setGender(adminVO.getGender());
            admin.setUserName(adminVO.getUserName());
            admin.setNickName(adminVO.getNickName());
            admin.setRoleUid(adminVO.getRoleUid());
            /* 设置为未审核状态 */
            admin.setStatus(EStatus.ENABLE);
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            //设置默认密码
            admin.setPassWord(encoder.encode(defaultPassword));
            adminService.save(admin);
            //这里需要通过SMS模块，发送邮件告诉初始密码
            return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_SUCCESS);
        }
        return ResultUtil.result(SysConf.ERROR, MessageConf.ENTITY_EXIST);
    }

    @Override
    public String editAdmin(AdminVO adminVO) {
        Admin admin = adminService.getById(adminVO.getUid());
        if (admin != null) {
            //判断修改的对象是否是超级管理员，超级管理员不能修改用户名
            if (admin.getUserName().equals(SysConf.ADMIN) && !adminVO.getUserName().equals(SysConf.ADMIN)) {
                return ResultUtil.result(SysConf.ERROR, "超级管理员用户名必须为admin");
            }
            QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
            queryWrapper.eq(SQLConf.USER_NAME, adminVO.getUserName());
            List<Admin> adminList = adminService.list(queryWrapper);
            if (adminList != null) {
                for (Admin item : adminList) {
                    if (item.getUid().equals(adminVO.getUid())) {
                        continue;
                    } else {
                        return ResultUtil.result(SysConf.ERROR, "修改失败，用户名存在");
                    }
                }
            }
        }
        admin.setUserName(adminVO.getUserName());
        admin.setAvatar(adminVO.getAvatar());
        admin.setNickName(adminVO.getNickName());
        admin.setGender(adminVO.getGender());
        admin.setEmail(adminVO.getEmail());
        admin.setQqNumber(adminVO.getQqNumber());
        admin.setGithub(adminVO.getGithub());
        admin.setGitee(adminVO.getGitee());
        admin.setOccupation(adminVO.getOccupation());
        admin.setUpdateTime(new Date());
        // 无法直接修改密码，只能通过重置密码完成密码修改
        admin.setPassWord(null);
        admin.updateById();

        // 修改成功后，判断是否更改了RoleUid，更新redis中admin的URL访问路径
        if (!admin.getRoleUid().equals(admin.getRoleUid())) {
            redisUtil.delete(RedisConf.ADMIN_VISIT_MENU + RedisConf.SEGMENTATION + admin.getUid());
        }
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }

    @Override
    public String editMe(AdminVO adminVO) {
        HttpServletRequest request = RequestHolder.getRequest();
        if (request.getAttribute(SysConf.ADMIN_UID) == null || request.getAttribute(SysConf.ADMIN_UID) == "") {
            return ResultUtil.result(SysConf.ERROR, MessageConf.OPERATION_FAIL);
        }
        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());
        admin.setAvatar(adminVO.getAvatar());
        admin.setNickName(adminVO.getNickName());
        admin.setGender(adminVO.getGender());
        admin.setEmail(adminVO.getEmail());
        admin.setQqNumber(adminVO.getQqNumber());
        admin.setGithub(adminVO.getGithub());
        admin.setGitee(adminVO.getGitee());
        admin.setOccupation(adminVO.getOccupation());
        admin.setSummary(adminVO.getSummary());
        admin.setPersonResume(adminVO.getPersonResume());
        admin.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }

    @Override
    public String changePwd(String oldPwd, String newPwd) {
        HttpServletRequest request = RequestHolder.getRequest();
        if (request.getAttribute(SysConf.ADMIN_UID) == null || request.getAttribute(SysConf.ADMIN_UID) == "") {
            return ResultUtil.result(SysConf.ERROR, MessageConf.INVALID_TOKEN);
        }
        if (StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(newPwd)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }

        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        boolean isPassword = encoder.matches(oldPwd, admin.getPassWord());

        if (isPassword) {
            admin.setPassWord(encoder.encode(newPwd));
            admin.setUpdateTime(new Date());
            admin.updateById();
            return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
        } else {
            return ResultUtil.result(SysConf.ERROR, MessageConf.ERROR_PASSWORD);
        }
    }

    @Override
    public String resetPwd(AdminVO adminVO) {
        String defaultPassword = sysParamsService.getSysParamsValueByKey(SysConf.SYS_DEFAULT_PASSWORD);
        if (StringUtils.isEmpty(defaultPassword)) {
            throw new UpdateException(MessageConf.PLEASE_CONFIGURE_PASSWORD);
        }
        Admin admin = adminService.getById(adminVO.getUid());
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        admin.setPassWord(encoder.encode(defaultPassword));
        admin.setUpdateTime(new Date());
        admin.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }

    @Override
    public String deleteBatchAdmin(List<String> adminUids) {
        if (adminUids.isEmpty()) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        Collection<Admin> adminList = adminService.listByIds(adminUids);

        for (Admin admin : adminList) {
            if (SysConf.ADMIN.equals(admin.getUserName())) {
                return ResultUtil.result(SysConf.ERROR, MessageConf.DELETE_ADMIN_ACCOUNT_ERROR);
            }
            admin.setUpdateTime(new Date());
            admin.setStatus(EStatus.DISABLED);
        }
        adminService.updateBatchById(adminList);
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
    }

    @Override
    public String forceLogout(List<String> tokenList) {
        if (tokenList == null || tokenList.size() == 0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        List<String> keyList = new ArrayList<>();
        String keyPrefix = RedisConf.LOGIN_TOKEN_KEY + RedisConf.SEGMENTATION;
        for (String token : tokenList) {
            keyList.add(keyPrefix + token);
        }
        redisUtil.delete(keyList);
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }
}
