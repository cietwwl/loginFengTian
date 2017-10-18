package com.zxz.service;

import org.json.JSONObject;

/**向外部提供数据的接口
 * @author Administrator
 *
 */
public interface DateService {

	
	/**得到在线
	 * @return
	 */
	JSONObject getGameJSONObject();
	
	/**得到房间的map
	 * @return
	 */
	String getRoomJSONObject();
	
	/**总的在线人数
	 * @return
	 */
	int getTotalOneLineUser();
	
	
	/**实时在线登录数
	 * @return
	 */
	int getTotalLoginLineUser();
	
	
	/**向房间里面发送，文件信息
	 * @param roomId
	 */
	void sendFilePathToRoom(String roomId,String fileName,String userId);
	
	/**得到房间的详细信息
	 * @param roomId 房间号
	 * @return 房间的详细信息
	 */
	String getRoomDetail(String roomId);
	
	
	/**得到总的在线人数和总的房间数
	 * @return
	 */
	String getTotalOnlineUserAndTotalRoom();
	
	/**根据房间号得到房间是否存在
	 * @return
	 */
	String isHaveRoomWithRoomId(String roomId);
	
	
	/**根据房间号得到房间是否存在
	 * @return
	 */
	String isUserInRoomWithRoomId(String roomId,String userId);
	
	
	String isUserInRoomWithRoomId(String roomId);
	/**解散房间 
	 * @param roomId
	 * @return 0 解散失败，1解散成功
	 */
	int disbandRoom(String roomId);
	/**得到房间的列表
	 * @param userId 创建房间的用户id 
	 * @return
	 */
	String getRoomList(int userId);
	/**
	 * 创建房间
	 * @param userId
	 * @return
	 */
	int checkCard(int userId);

	int daiLiCreateRoom(String serviceStr);
	/**
	 * 检测是否有房间里面有该用户
	 * @param userId
	 * @return
	 */
	boolean isRoomsHaveThisUser(int userId);
}
