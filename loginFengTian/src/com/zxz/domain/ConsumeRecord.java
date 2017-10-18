package com.zxz.domain;

import java.util.Date;

public class ConsumeRecord {
    private Integer id;

    private Integer roomid; //房间号

    private Integer userid;//用户从id

    private Integer totalgame;//总局数

    private Date createdate;//创建时间

    private Integer type;//充值0,消费 1,会员充值 2

    private Integer total;//充值房卡数量
    
    private Integer sendCard;//赠送数量

    private Integer operateId;//运营id
    
    private float money;//充值的金额
    
    public Integer getSendCard() {
		return sendCard;
	}

	public void setSendCard(Integer sendCard) {
		this.sendCard = sendCard;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public Integer getOperateId() {
		return operateId;
	}

	public void setOperateId(Integer operateId) {
		this.operateId = operateId;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomid() {
        return roomid;
    }

    public void setRoomid(Integer roomid) {
        this.roomid = roomid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getTotalgame() {
        return totalgame;
    }

    public void setTotalgame(Integer totalgame) {
        this.totalgame = totalgame;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}