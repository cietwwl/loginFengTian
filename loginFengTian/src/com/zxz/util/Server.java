package com.zxz.util;

/**
 * @author Administrator
 * 
 */
public class Server {

	public Server() {
	}

	
	/**
	 * 服务器ID
	 */
	private int id;
	
	private String ip;
	
	/**
	 * RPC端口号
	 */
	private int rpcPort;
	
	
	/**
	 * 游戏端口号
	 */
	private int gamePort;
	
	/**
	 * 总的房间数
	 */
	private int totalRoom;
	
	
	public int getGamePort() {
		return gamePort;
	}

	public void setGamePort(int gamePort) {
		this.gamePort = gamePort;
	}

	public int getTotalRoom() {
		return totalRoom;
	}

	public void setTotalRoom(int totalRoom) {
		this.totalRoom = totalRoom;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	

	public int getRpcPort() {
		return rpcPort;
	}

	public void setRpcPort(int rpcPort) {
		this.rpcPort = rpcPort;
	}

	@Override
	public String toString() {
		return "Server [id=" + id + ", ip=" + ip + ", rpcPort=" + rpcPort
				+ ", totalRoom=" + totalRoom + "]";
	}

	
	
}