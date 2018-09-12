package com.moxi.mogublog.admin.config;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.moxi.mogublog.admin.global.SysConf;

@Component
public class MetaObjectHandlerConfig implements MetaObjectHandler {
	
  Logger log = LogManager.getLogger(MetaObjectHandlerConfig.class);	
  @Override
  public void insertFill(MetaObject metaObject) {
	log.info("插入方法填充");
    setFieldValByName(SysConf.createtime, new Date(), metaObject);
    setFieldValByName(SysConf.updatetime, new Date(), metaObject);
  }

  @Override
  public void updateFill(MetaObject metaObject) {
	  log.info("更新方法填充");
	  setFieldValByName(SysConf.updatetime, new Date(), metaObject);
  }
}
