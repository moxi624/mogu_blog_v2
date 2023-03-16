package com.moxi.mogublog.xo.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.commons.entity.Admin;
import com.moxi.mogublog.commons.entity.OnlineAdmin;
import com.moxi.mogublog.commons.entity.Role;
import com.moxi.mogublog.commons.entity.Storage;
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
import com.moxi.mougblog.base.global.Constants;
import com.moxi.mougblog.base.holder.RequestHolder;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 管理员表 服务实现类
 *
 * @author 陌溪
 * @since 2018-09-04
 */
@Service
@Slf4j
public class AdminServiceImpl extends SuperServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    AdminService adminService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    SysParamsService sysParamsService;
    @Resource
    private AdminMapper adminMapper;
    @Autowired
    private WebUtil webUtil;
    @Resource
    private PictureFeignClient pictureFeignClient;
    @Autowired
    private RoleService roleService;

    @Override
    public Admin getAdminByUid(String uid) {
        return adminMapper.getAdminByUid(uid);
    }

    @Override
    public String getOnlineAdminList(AdminVO adminVO) {
        // 拼装分页信息
        int pageSize = adminVO.getPageSize().intValue();
        int currentPage = adminVO.getCurrentPage().intValue();
        AtomicReference<Integer> total = new AtomicReference<Integer>(0);
        int startIndex = Math.max((currentPage - 1) * pageSize, 0);

        // 获取Redis中匹配的key
        Set<String> keys = redisUtil.getRedisTemplate().execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keySetTemp = new ConcurrentSkipListSet<>();
            int index = 0;
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions()
                    .match(RedisConf.LOGIN_TOKEN_KEY + "*")
                    .count(100000)
                    .build())) {

                // 获取登录用户的key
                while (cursor.hasNext()) {
                    index ++;
                    // 先偏移起始位置个数据
                    if (index<startIndex){
                        cursor.next();
                        continue;
                    }
                    String key = new String(cursor.next(), CharsetUtil.UTF_8);

                    // 获取需要的key
                    if (keySetTemp.size() <= pageSize) {
                        keySetTemp.add(key);
                    }

                }
                total.set(index);
            } catch (Exception e) {
                log.error("Redis Scan get Exception：{}", ExceptionUtil.stacktraceToOneLineString(e), e);
                return new ConcurrentSkipListSet<>();
            }
            return keySetTemp;
        });

        // 获取在线用户数据
        List<String> onlineAdminJsonList = redisUtil.multiGet(keys);
        List<OnlineAdmin> onlineAdminList = new ArrayList<>();
        for (String item : onlineAdminJsonList) {
            OnlineAdmin onlineAdmin = JsonUtils.jsonToPojo(item, OnlineAdmin.class);
            // 数据脱敏【移除用户的token令牌】
            onlineAdmin.setToken("");
            onlineAdminList.add(onlineAdmin);
        }
        Page<OnlineAdmin> page = new Page<>();
        page.setCurrent(currentPage);
        page.setTotal(total.get());
        page.setSize(pageSize);
        page.setRecords(onlineAdminList);
        return ResultUtil.successWithData(page);
    }

    @Override
    public Admin getAdminByUser(String userName) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.USER_NAME, userName);
        queryWrapper.last(SysConf.LIMIT_ONE);
        //清空密码，防止泄露
        Admin admin = adminService.getOne(queryWrapper);
        admin.setPassWord(null);
        //获取图片
        if (StringUtils.isNotEmpty(admin.getAvatar())) {
            String pictureList = this.pictureFeignClient.getPicture(admin.getAvatar(), Constants.SYMBOL_COMMA);
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
            String pictureList = this.pictureFeignClient.getPicture(admin.getAvatar(), Constants.SYMBOL_COMMA);
            admin.setPhotoList(webUtil.getPicture(pictureList));
        }
        return admin;
    }

    @Override
    public void addOnlineAdmin(Admin admin, Long expirationSecond) {
        HttpServletRequest request = RequestHolder.getRequest();
        Map<String, String> map = IpUtils.getOsAndBrowserInfo(request);
        String os = map.get(SysConf.OS);
        String browser = map.get(SysConf.BROWSER);
        String ip = IpUtils.getIpAddr(request);
        OnlineAdmin onlineAdmin = new OnlineAdmin();
        onlineAdmin.setAdminUid(admin.getUid());
        onlineAdmin.setTokenId(admin.getTokenUid());
        onlineAdmin.setToken(admin.getValidCode());
        onlineAdmin.setOs(os);
        onlineAdmin.setBrowser(browser);
        onlineAdmin.setIpaddr(ip);
        onlineAdmin.setLoginTime(DateUtils.getNowTime());
        onlineAdmin.setRoleName(admin.getRole().getRoleName());
        onlineAdmin.setUserName(admin.getUserName());
        onlineAdmin.setExpireTime(DateUtils.getDateStr(new Date(), expirationSecond));
        //从Redis中获取IP来源
        String jsonResult = redisUtil.get(RedisConf.IP_SOURCE + Constants.SYMBOL_COLON + ip);
        if (StringUtils.isEmpty(jsonResult)) {
            String addresses = IpUtils.getAddresses(SysConf.IP + SysConf.EQUAL_TO + ip, SysConf.UTF_8);
            if (StringUtils.isNotEmpty(addresses)) {
                onlineAdmin.setLoginLocation(addresses);
                redisUtil.setEx(RedisConf.IP_SOURCE + Constants.SYMBOL_COLON + ip, addresses, 24, TimeUnit.HOURS);
            }
        } else {
            onlineAdmin.setLoginLocation(jsonResult);
        }
        // 将登录的管理员存储到在线用户表
        redisUtil.setEx(RedisConf.LOGIN_TOKEN_KEY + RedisConf.SEGMENTATION + admin.getValidCode(), JsonUtils.objectToJson(onlineAdmin), expirationSecond, TimeUnit.SECONDS);
        // 在维护一张表，用于 uuid - token 互相转换
        redisUtil.setEx(RedisConf.LOGIN_UUID_KEY + RedisConf.SEGMENTATION + admin.getTokenUid(), admin.getValidCode(), expirationSecond, TimeUnit.SECONDS);
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
        List<String> adminUidList = new ArrayList<>();
        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getAvatar())) {
                fileUids.append(item.getAvatar() + SysConf.FILE_SEGMENTATION);
            }
            adminUidList.add(item.getUid());
        });

        Map<String, String> pictureMap = new HashMap<>(Constants.NUM_TEN);
        if (fileUids != null) {
            pictureResult = this.pictureFeignClient.getPicture(fileUids.toString(), SysConf.FILE_SEGMENTATION);
        }
        List<Map<String, Object>> picList = webUtil.getPictureMap(pictureResult);
        picList.forEach(item -> {
            pictureMap.put(item.get(SQLConf.UID).toString(), item.get(SQLConf.URL).toString());
        });

        // 获取用户的网盘存储空间
        String storageListJson = pictureFeignClient.getStorageByAdminUid(adminUidList);
        List<Storage> storageList = webUtil.getList(storageListJson, Storage.class);
        Map<String, Storage> storageMap = new HashMap<>();
        storageList.forEach(item -> {
            storageMap.put(item.getAdminUid(), item);
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

            // 设置已用容量大小和最大容量
            Storage storage = storageMap.get(item.getUid());
            if(storage != null) {
                item.setStorageSize(storage.getStorageSize());
                item.setMaxStorageSize(storage.getMaxStorageSize());
            } else {
                // 如果没有，默认为0
                item.setStorageSize(0L);
                item.setMaxStorageSize(0L);
            }
        }
        return ResultUtil.successWithData(pageList);
    }

    @Override
    public String addAdmin(AdminVO adminVO) {

        String mobile = adminVO.getMobile();
        String userName = adminVO.getUserName();
        String email = adminVO.getEmail();
        if (StringUtils.isEmpty(userName)) {
            return ResultUtil.errorWithMessage(MessageConf.PARAM_INCORRECT);
        }
        if (StringUtils.isEmpty(email) && StringUtils.isEmpty(mobile)) {
            return ResultUtil.errorWithMessage("邮箱和手机号至少一项不能为空");
        }
        String defaultPassword = sysParamsService.getSysParamsValueByKey(SysConf.SYS_DEFAULT_PASSWORD);
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
            // 设置为未审核状态
            admin.setStatus(EStatus.ENABLE);
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            //设置默认密码
            admin.setPassWord(encoder.encode(defaultPassword));
            adminService.save(admin);
            //TODO 这里需要通过SMS模块，发送邮件告诉初始密码

            // 更新成功后，同时申请网盘存储空间
            String maxStorageSize = sysParamsService.getSysParamsValueByKey(SysConf.MAX_STORAGE_SIZE);
            // 初始化网盘的容量, 单位 B
            pictureFeignClient.initStorageSize(admin.getUid(), StringUtils.getLong(maxStorageSize, 0L) * 1024 * 1024);
            return ResultUtil.successWithMessage(MessageConf.INSERT_SUCCESS);
        }
        return ResultUtil.errorWithMessage(MessageConf.ENTITY_EXIST);
    }

    @Override
    public String editAdmin(AdminVO adminVO) {
        Admin admin = adminService.getById(adminVO.getUid());
        Assert.notNull(admin, MessageConf.PARAM_INCORRECT);
        //判断修改的对象是否是admin，admin的用户名必须是admin
        if (admin.getUserName().equals(SysConf.ADMIN) && !adminVO.getUserName().equals(SysConf.ADMIN)) {
            return ResultUtil.errorWithMessage("超级管理员用户名必须为admin");
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
                    return ResultUtil.errorWithMessage("修改失败，用户名存在");
                }
            }
        }

        // 判断是否更改了RoleUid，更新redis中admin的URL访问路径
        if (StringUtils.isNotEmpty(adminVO.getRoleUid()) && !adminVO.getRoleUid().equals(admin.getRoleUid())) {
            redisUtil.delete(RedisConf.ADMIN_VISIT_MENU + RedisConf.SEGMENTATION + admin.getUid());
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
        admin.setMobile(adminVO.getMobile());
        admin.setRoleUid(adminVO.getRoleUid());
        // 无法直接修改密码，只能通过重置密码完成密码修改
        admin.setPassWord(null);
        admin.updateById();

        // 更新完成后，判断是否调整了网盘的大小
        String result = pictureFeignClient.editStorageSize(admin.getUid(), adminVO.getMaxStorageSize() * 1024 * 1024);
        Map<String, String> resultMap = webUtil.getMessage(result);
        if(SysConf.SUCCESS.equals(resultMap.get(SysConf.CODE))) {
            return ResultUtil.successWithMessage(resultMap.get(SysConf.MESSAGE));
        } else {
            return ResultUtil.errorWithMessage(resultMap.get(SysConf.MESSAGE));
        }
    }

    @Override
    public String editMe(AdminVO adminVO) {
        String adminUid = RequestHolder.getAdminUid();
        if (StringUtils.isEmpty(adminUid)) {
            return ResultUtil.errorWithMessage(MessageConf.INVALID_TOKEN);
        }
        Admin admin = new Admin();
        // 【使用Spring工具类提供的深拷贝，减少大量模板代码】
        BeanUtils.copyProperties(adminVO, admin, SysConf.STATUS);
        admin.setUpdateTime(new Date());
        admin.updateById();
        return ResultUtil.successWithMessage(MessageConf.OPERATION_SUCCESS);
    }

    @Override
    public String changePwd(String oldPwd, String newPwd) {
        String adminUid = RequestHolder.getAdminUid();
        if (StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(newPwd)) {
            return ResultUtil.errorWithMessage(MessageConf.PARAM_INCORRECT);
        }
        Admin admin = adminService.getById(adminUid);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean isPassword = encoder.matches(oldPwd, admin.getPassWord());
        if (isPassword) {
            admin.setPassWord(encoder.encode(newPwd));
            admin.setUpdateTime(new Date());
            admin.updateById();
            return ResultUtil.successWithMessage(MessageConf.UPDATE_SUCCESS);
        } else {
            return ResultUtil.errorWithMessage(MessageConf.ERROR_PASSWORD);
        }
    }

    @Override
    public String resetPwd(AdminVO adminVO) {
        String defaultPassword = sysParamsService.getSysParamsValueByKey(SysConf.SYS_DEFAULT_PASSWORD);
        // 获取当前用户的管理员uid
        String adminUid = RequestHolder.getAdminUid();
        Admin admin = adminService.getById(adminVO.getUid());
        // 判断是否是admin重置密码【其它超级管理员，无法重置admin的密码】
        if (SysConf.ADMIN.equals(admin.getUserName()) && !admin.getUid().equals(adminUid)) {
            return ResultUtil.errorWithMessage(MessageConf.UPDATE_ADMIN_PASSWORD_FAILED);
        } else {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            admin.setPassWord(encoder.encode(defaultPassword));
            admin.setUpdateTime(new Date());
            admin.updateById();
            return ResultUtil.successWithMessage(MessageConf.UPDATE_SUCCESS);
        }
    }

    @Override
    public String deleteBatchAdmin(List<String> adminUidList) {
        boolean checkResult = StringUtils.checkUidList(adminUidList);
        if (!checkResult) {
            return ResultUtil.errorWithMessage(MessageConf.PARAM_INCORRECT);
        }
        List<Admin> adminList = new ArrayList<>();
        adminUidList.forEach(item -> {
            Admin admin = new Admin();
            admin.setUid(item);
            admin.setStatus(EStatus.DISABLED);
            admin.setUpdateTime(new Date());
            adminList.add(admin);
        });
        adminService.updateBatchById(adminList);
        return ResultUtil.successWithMessage(MessageConf.DELETE_SUCCESS);
    }

    @Override
    public String forceLogout(List<String> tokenUidList) {
        if (tokenUidList == null || tokenUidList.size() == 0) {
            return ResultUtil.errorWithMessage(MessageConf.PARAM_INCORRECT);
        }

        // 从Redis中通过TokenUid获取到用户的真实token
        List<String> tokenList = new ArrayList<>();
        tokenUidList.forEach(item -> {
            String token = redisUtil.get(RedisConf.LOGIN_UUID_KEY + RedisConf.SEGMENTATION + item);
            if(StringUtils.isNotEmpty(token)) {
                tokenList.add(token);
            }
        });

        // 根据token删除Redis中的在线用户
        List<String> keyList = new ArrayList<>();
        String keyPrefix = RedisConf.LOGIN_TOKEN_KEY + RedisConf.SEGMENTATION;
        for (String token : tokenList) {
            keyList.add(keyPrefix + token);
        }
        redisUtil.delete(keyList);
        return ResultUtil.successWithMessage(MessageConf.OPERATION_SUCCESS);
    }
}
