package com.zxz.domain;

import java.util.Date;

public class Zjcard {
	private Integer userid;// 用户ID

	private Integer roomcard;// 房卡数量

	private Date createdate;// 创建时间

	private String refreshToken;
	
	private Integer bangDing;//是否绑定 0 未绑定 1绑定 
	

	
	public Integer getBangDing() {
		return bangDing;
	}

	public void setBangDing(Integer bangDing) {
		this.bangDing = bangDing;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getRoomcard() {
		return roomcard;
	}

	public void setRoomcard(Integer roomcard) {
		this.roomcard = roomcard;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	@Override
	public String toString() {
		return "Zjcard [userid=" + userid + ", roomcard=" + roomcard
				+ ", createdate=" + createdate + "]";
	}
}