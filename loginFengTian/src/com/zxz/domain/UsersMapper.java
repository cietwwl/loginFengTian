package com.zxz.domain;

import java.util.Date;
/**
 * 用户与代理绑定表
 * @author Administrator
 *
 */
public class UsersMapper {
	private Integer id;
	private Integer userid;//用户id
	private Integer operateUserId;//代理id
	private Integer level;//代理等级
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getOperateUserId() {
		return operateUserId;
	}

	public void setOperateUserId(Integer operateUserId) {
		this.operateUserId = operateUserId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	private Date createDate;//绑定日期
	private String unionid;//用户unionid
	
	@Override
	public String toString() {
		return "UsersMapper [id=" + id + ", userid=" + userid
				+ ", operateUserId=" + operateUserId + ", level=" + level
				+ ", createDate=" + createDate + ", unionid=" + unionid + "]";
	}
	
	 
}
