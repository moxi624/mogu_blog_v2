package com.moxi.mogublog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.commons.entity.User;
import com.moxi.mogublog.commons.feign.PictureFeignClient;
import com.moxi.mogublog.utils.*;
import com.moxi.mogublog.xo.global.MessageConf;
import com.moxi.mogublog.xo.global.RedisConf;
import com.moxi.mogublog.xo.global.SQLConf;
import com.moxi.mogublog.xo.global.SysConf;
import com.moxi.mogublog.xo.mapper.UserMapper;
import com.moxi.mogublog.xo.service.SysParamsService;
import com.moxi.mogublog.xo.service.UserService;
import com.moxi.mogublog.xo.utils.WebUtil;
import com.moxi.mogublog.xo.vo.UserVO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.exception.exceptionType.InsertException;
import com.moxi.mougblog.base.exception.exceptionType.QueryException;
import com.moxi.mougblog.base.global.BaseSQLConf;
import com.moxi.mougblog.base.global.Constants;
import com.moxi.mougblog.base.global.ErrorCode;
import com.moxi.mougblog.base.holder.RequestHolder;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户表 服务实现类
 *
 * @author 陌溪
 * @since 2018-09-04
 */
@Service
public class UserServiceImpl extends SuperServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    WebUtil webUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SysParamsService sysParamsService;
    @Resource
    private PictureFeignClient pictureFeignClient;

    @Override
    public User insertUserInfo(HttpServletRequest request, String response) {
        Map<String, Object> map = JsonUtils.jsonToMap(response);
        boolean exist = false;
        User user = new User();
        Map<String, Object> data = JsonUtils.jsonToMap(JsonUtils.objectToJson(map.get(SysConf.DATA)));
        if (data.get(SysConf.UUID) != null && data.get(SysConf.SOURCE) != null) {
            if (getUserBySourceAnduuid(data.get(SysConf.SOURCE).toString(), data.get(SysConf.UUID).toString()) != null) {
                user = getUserBySourceAnduuid(data.get(SysConf.SOURCE).toString(), data.get(SysConf.UUID).toString());
                exist = true;
            }
        } else {
            log.error("未获取到uuid或source");
            throw new InsertException(ErrorCode.INSERT_DEFAULT_ERROR, MessageConf.INSERT_DEFAULT_ERROR);
        }

        if (data.get(SysConf.EMAIL) != null) {
            user.setEmail(data.get(SysConf.EMAIL).toString());
        }
        if (data.get(SysConf.AVATAR) != null) {
            user.setAvatar(data.get(SysConf.AVATAR).toString());
        }
        if (data.get(SysConf.NICKNAME) != null) {
            user.setNickName(data.get(SysConf.NICKNAME).toString());
        }
        user.setLoginCount(user.getLoginCount() + 1);
        user.setLastLoginTime(new Date());
        user.setLastLoginIp(IpUtils.getIpAddr(request));
        if (exist) {
            user.updateById();
        } else {
            user.setUuid(data.get(SysConf.UUID).toString());
            user.setSource(data.get(SysConf.SOURCE).toString());
            user.setUserName("mg".concat(user.getSource()).concat(user.getUuid()));
            //产生(0,999999]之间的随机数
            Integer randNum = (int) (Math.random() * (999999) + 1);
            //进行六位数补全
            String workPassWord = String.format("%06d", randNum);
            user.setPassWord(workPassWord);
            user.insert();
        }
        return user;
    }

    @Override
    public User getUserBySourceAnduuid(String source, String uuid) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(BaseSQLConf.UUID, uuid).eq(BaseSQLConf.SOURCE, source);
        return userService.getOne(queryWrapper);
    }

    @Override
    public Integer getUserCount(int status) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(BaseSQLConf.STATUS, status);
        return userService.count(queryWrapper);
    }

    @Override
    public User serRequestInfo(User user) {
        HttpServletRequest request = RequestHolder.getRequest();
        Map<String, String> map = IpUtils.getOsAndBrowserInfo(request);
        String os = map.get("OS");
        String browser = map.get("BROWSER");
        String ip = IpUtils.getIpAddr(request);
        user.setLastLoginIp(ip);
        user.setOs(os);
        user.setBrowser(browser);
        user.setLastLoginTime(new Date());
        //从Redis中获取IP来源
        String jsonResult = stringRedisTemplate.opsForValue().get(RedisConf.IP_SOURCE + Constants.SYMBOL_COLON + ip);
        if (StringUtils.isEmpty(jsonResult)) {
            String addresses = IpUtils.getAddresses(SysConf.IP + Constants.SYMBOL_RIGHT_EQUAL + ip, "utf-8");
            if (StringUtils.isNotEmpty(addresses)) {
                user.setIpSource(addresses);
                stringRedisTemplate.opsForValue().set(RedisConf.IP_SOURCE + Constants.SYMBOL_COLON + ip, addresses, 24, TimeUnit.HOURS);
            }
        } else {
            user.setIpSource(jsonResult);
        }
        return user;
    }

    @Override
    public List<User> getUserListByIds(List<String> ids) {
        List<User> userList = new ArrayList<>();
        if (ids == null || ids.size() == 0) {
            return userList;
        }
        Collection<User> userCollection = userService.listByIds(ids);
        userCollection.forEach(item -> {
            userList.add(item);
        });
        return userList;
    }

    @Override
    public IPage<User> getPageList(UserVO userVO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 查询用户名
        if (StringUtils.isNotEmpty(userVO.getKeyword()) && !StringUtils.isEmpty(userVO.getKeyword().trim())) {
            queryWrapper.like(SQLConf.USER_NAME, userVO.getKeyword().trim()).or().like(SQLConf.NICK_NAME, userVO.getKeyword().trim());
        }
        if (StringUtils.isNotEmpty(userVO.getSource()) && !StringUtils.isEmpty(userVO.getSource().trim())) {
            queryWrapper.eq(SQLConf.SOURCE, userVO.getSource().trim());
        }
        if (userVO.getCommentStatus() != null) {
            queryWrapper.eq(SQLConf.COMMENT_STATUS, userVO.getCommentStatus());
        }
        queryWrapper.select(User.class, i -> !i.getProperty().equals(SQLConf.PASS_WORD));
        Page<User> page = new Page<>();
        page.setCurrent(userVO.getCurrentPage());
        page.setSize(userVO.getPageSize());
        queryWrapper.ne(SQLConf.STATUS, EStatus.DISABLED);

        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        IPage<User> pageList = userService.page(page, queryWrapper);

        List<User> list = pageList.getRecords();

        final StringBuffer fileUids = new StringBuffer();
        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getAvatar())) {
                fileUids.append(item.getAvatar() + SysConf.FILE_SEGMENTATION);
            }
        });

        Map<String, String> pictureMap = new HashMap<>();
        String pictureResult = null;

        if (fileUids != null) {
            pictureResult = this.pictureFeignClient.getPicture(fileUids.toString(), SysConf.FILE_SEGMENTATION);
        }
        List<Map<String, Object>> picList = webUtil.getPictureMap(pictureResult);

        picList.forEach(item -> {
            pictureMap.put(item.get(SQLConf.UID).toString(), item.get(SQLConf.URL).toString());
        });

        for (User item : list) {
            //获取图片
            if (StringUtils.isNotEmpty(item.getAvatar())) {
                List<String> pictureUidsTemp = StringUtils.changeStringToString(item.getAvatar(), SysConf.FILE_SEGMENTATION);
                List<String> pictureListTemp = new ArrayList<>();
                pictureUidsTemp.forEach(picture -> {
                    if (pictureMap.get(picture) != null && pictureMap.get(picture) != "") {
                        pictureListTemp.add(pictureMap.get(picture));
                    }
                });
                if (pictureListTemp.size() > 0) {
                    item.setPhotoUrl(pictureListTemp.get(0));
                }
            }
        }
        pageList.setRecords(list);
        return pageList;
    }

    @Override
    public String addUser(UserVO userVO) {
        User user = new User();
        // 字段拷贝【将userVO中的内容拷贝至user】
        BeanUtils.copyProperties(userVO, user, SysConf.STATUS);
        String defaultPassword = sysParamsService.getSysParamsValueByKey(SysConf.SYS_DEFAULT_PASSWORD);
        user.setPassWord(MD5Utils.string2MD5(defaultPassword));
        user.setSource("MOGU");
        user.insert();
        return ResultUtil.successWithMessage(MessageConf.INSERT_SUCCESS);
    }

    @Override
    public String editUser(UserVO userVO) {
        User user = userService.getById(userVO.getUid());
        user.setUserName(userVO.getUserName());
        user.setEmail(userVO.getEmail());
        user.setStartEmailNotification(userVO.getStartEmailNotification());
        user.setOccupation(userVO.getOccupation());
        user.setGender(userVO.getGender());
        user.setQqNumber(userVO.getQqNumber());
        user.setSummary(userVO.getSummary());
        user.setBirthday(userVO.getBirthday());
        user.setAvatar(userVO.getAvatar());
        user.setNickName(userVO.getNickName());
        user.setUserTag(userVO.getUserTag());
        user.setCommentStatus(userVO.getCommentStatus());
        user.setUpdateTime(new Date());
        user.updateById();
        return ResultUtil.successWithMessage(MessageConf.UPDATE_SUCCESS);
    }

    @Override
    public String deleteUser(UserVO userVO) {
        User user = userService.getById(userVO.getUid());
        user.setStatus(EStatus.DISABLED);
        user.setUpdateTime(new Date());
        user.updateById();
        return ResultUtil.successWithMessage(MessageConf.DELETE_SUCCESS);
    }

    @Override
    public String resetUserPassword(UserVO userVO) {
        String defaultPassword = sysParamsService.getSysParamsValueByKey(SysConf.SYS_DEFAULT_PASSWORD);
        User user = userService.getById(userVO.getUid());
        user.setPassWord(MD5Utils.string2MD5(defaultPassword));
        user.setUpdateTime(new Date());
        user.updateById();
        return ResultUtil.successWithMessage(MessageConf.OPERATION_SUCCESS);
    }
}
