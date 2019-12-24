package com.moxi.mogublog.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MetaObjectHandlerConfig implements MetaObjectHandler {

    Logger log = LoggerFactory.getLogger(MetaObjectHandlerConfig.class);

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("插入方法填充");
        setFieldValByName("create_time", new Date(), metaObject);
        setFieldValByName("update_time", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("更新方法填充");
        setFieldValByName("update_time", new Date(), metaObject);
    }
}
