package com.zxz.domain;

import java.util.Date;

public class User {
	
    private Integer id;

    private String openid;

    private String nickName; //昵称

    private String sex;//性别

    private String province;//省份

    private String city;//城市

    private String country;//国家

    private String unionid;

    private String userName;

    private String password;

    private String roomId;//房间号

    private Integer roomCard;//房卡数

    private String refreshToken;

    private Date createdate;//注册时间

    private String headimgurl;//头像
    
    private int recommendId;//推荐人id
    
    private int consumeCard;//消耗的房卡数
    
    private Integer ut;
    
    private int isDaiLi=0;//是否是代理 0 不是代理 1是代理
    
    public int getIsDaiLi() {
		return isDaiLi;
	}

	public void setIsDaiLi(int isDaiLi) {
		this.isDaiLi = isDaiLi;
	}
    
    public Integer getUt() {
		return ut;
	}

	public void setUt(Integer ut) {
		this.ut = ut;
	}

	public int getConsumeCard() {
		return consumeCard;
	}

	public void setConsumeCard(int consumeCard) {
		this.consumeCard = consumeCard;
	}

	public int getRecommendId() {
		return recommendId;
	}

	public void setRecommendId(int recommendId) {
		this.recommendId = recommendId;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickname) {
        this.nickName = nickname == null ? null : nickname.trim();
    }

    public String getSex() {
		return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid == null ? null : unionid.trim();
    }

    public String getUserName() {
        return this.getNickName();
    }

    public void setUserName(String username) {
        this.userName = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getRoomid() {
        return roomId==null?0+"":roomId;
    }

    public void setRoomid(String roomid) {
        this.roomId = roomid == null ? null : roomid.trim();
    }

    public Integer getRoomCard() {
        return roomCard;
    }

    public void setRoomCard(Integer roomcard) {
        this.roomCard = roomcard;
    }

    public String getRefreshtoken() {
        return refreshToken;
    }

    public void setRefreshtoken(String refreshtoken) {
        this.refreshToken = refreshtoken == null ? null : refreshtoken.trim();
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", openid=" + openid + ", nickName="
				+ nickName + ", sex=" + sex + ", province=" + province
				+ ", city=" + city + ", country=" + country + ", unionid="
				+ unionid + ", userName=" + userName + ", password=" + password
				+ ", roomId=" + roomId + ", roomCard=" + roomCard
				+ ", refreshToken=" + refreshToken + ", createdate="
				+ createdate + ", headimgurl=" + headimgurl + ", recommendId="
				+ recommendId + ", consumeCard=" + consumeCard + ", ut=" + ut
				+ ", isDaiLi=" + isDaiLi + "]";
	}
    
    
}