package com.zxz.domain;

import java.util.Date;

public class Pdkcard {
    private Integer userid;

    private Integer roomcard;

    private Date createdate;
    
    private String refreshToken;
    
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
}