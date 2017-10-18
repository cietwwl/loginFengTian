package com.zxz.domain;

import java.io.Serializable;
import java.util.Date;

/**运营人员
 * @author Administrator
 *
 */
public class OperateUser implements Serializable{
	
    private Integer id;

    private String username;//用户名

    private String password;//密码
    
    private String address;//地址
    
    private String mobile;//手机号码

    private Integer pid;//父id

    private Date createDate;//注册时间
    
    private Integer roomCard;//房卡数量
    
    private Integer consumeCard;//房卡消耗数量
    
    private Integer recommendId;//推荐号
    
    private Integer isDel;//0未删除 1删除 
    
    private Integer beRecommendId;//谁推荐的
    
	public Integer getBeRecommendId() {
		return beRecommendId;
	}

	public void setBeRecommendId(Integer beRecommendId) {
		this.beRecommendId = beRecommendId;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Integer getRecommendId() {
		return recommendId;
	}

	public void setRecommendId(Integer recommendId) {
		this.recommendId = recommendId;
	}

	public Integer getRoomCard() {
		return roomCard;
	}

	public void setRoomCard(Integer roomCard) {
		this.roomCard = roomCard;
	}

	public Integer getConsumeCard() {
		return consumeCard;
	}

	public void setConsumeCard(Integer consumeCard) {
		this.consumeCard = consumeCard;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

	@Override
	public String toString() {
		return "OperateUser [id=" + id + ", username=" + username
				+ ", password=" + password + ", address=" + address
				+ ", mobile=" + mobile + ", pid=" + pid + ", createDate="
				+ createDate + ", roomCard=" + roomCard + ", consumeCard="
				+ consumeCard + ", recommendId=" + recommendId + ", isDel="
				+ isDel + "]";
	}

	
    
    
}