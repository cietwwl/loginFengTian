package com.zxz.domain;

import java.util.Date;

public class Room {

	int roomNumber;//房间号
	int total;//局数
	int zhama;//扎码数
	User createUser;//创建人
	Date createDate;//创建时间
	
	public int getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getZhama() {
		return zhama;
	}
	public void setZhama(int zhama) {
		this.zhama = zhama;
	}
	public User getCreateUser() {
		return createUser;
	}
	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Override
	public String toString() {
		return "Room [roomNumber=" + roomNumber + ", total=" + total
				+ ", zhama=" + zhama + ", createUser=" + createUser
				+ ", createDate=" + createDate + "]";
	}
	
}
