package com.moxi.mougblog.base.entity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;


/**
 * Entity基类
 * @author xuzhixiang
 * @date 2017年9月16日10:55:35
 *
 */
@SuppressWarnings("rawtypes")
public class BaseEntity<T extends Model> extends Model<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4851055162892178225L;
	
	@TableId("id")
	private String uid; // 唯一uid	
	private int oid; // oid
	private int status; // 0 失效  1 生效
	private Timestamp createtime; //创建时间
	private Timestamp updatetime; //更新时间
	
	public BaseEntity() {
		this.uid = getUUID();
		this.status = 1;
		this.createtime = new Timestamp(System.currentTimeMillis()); 
		this.updatetime = new Timestamp(System.currentTimeMillis()); 
	}
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public int getOid() {
		return oid;
	}
	
	public void setOid(int oid) {
		this.oid = oid;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public Timestamp getCreatetime() {
		return createtime;
	}
	
	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	
	public Timestamp getUpdatetime() {
		return updatetime;
	}
	
	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}
	
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		return uuid;
	}
	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return null;
	}

}