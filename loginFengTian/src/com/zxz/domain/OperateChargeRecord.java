package com.zxz.domain;

import java.util.Date;

/**
 * 代理充值记录
 * 
 * @author Administrator
 */
public class OperateChargeRecord {

	private Integer id;

	private Integer operateId;// 操作人ID

	private Integer roomCard;// 房卡数量

	private Integer sendCard;// 赠送数量
	
	private Float money;// 充值金额

	private Integer chargeOperateId;// 被充值人id

	private Integer type;// 类型 0 购买1充值 2退卡

	private Date createDate;// 创建时间

	
	public Integer getSendCard() {
		return sendCard;
	}

	public void setSendCard(Integer sendCard) {
		this.sendCard = sendCard;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOperateId() {
		return operateId;
	}

	public void setOperateId(Integer operateId) {
		this.operateId = operateId;
	}

	public Integer getRoomCard() {
		return roomCard;
	}

	public void setRoomCard(Integer roomCard) {
		this.roomCard = roomCard;
	}

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}

	public Integer getChargeOperateId() {
		return chargeOperateId;
	}

	public void setChargeOperateId(Integer chargeOperateId) {
		this.chargeOperateId = chargeOperateId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
}
