package com.moxi.mogublog.admin.config;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.moxi.mogublog.admin.global.SQLConf;

@Component
public class MetaObjectHandlerConfig implements MetaObjectHandler {
	
  Logger log = LogManager.getLogger(MetaObjectHandlerConfig.class);	
  @Override
  public void insertFill(MetaObject metaObject) {
	log.info("插入方法填充");
    setFieldValByName(SQLConf.CREATE_TIME, new Date(), metaObject);
    setFieldValByName(SQLConf.UPDATE_TIME, new Date(), metaObject);
  }

  @Override
  public void updateFill(MetaObject metaObject) {
	  log.info("更新方法填充");
	  setFieldValByName(SQLConf.UPDATE_TIME, new Date(), metaObject);
  }
}
