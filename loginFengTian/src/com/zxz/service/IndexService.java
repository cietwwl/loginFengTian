package com.zxz.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zxz.dao.OneRoomDao;
import com.zxz.dao.UserMapper;
import com.zxz.dao.UserScoreDao;
import com.zxz.dao.impl.OneRoomDaoImpl;
import com.zxz.dao.impl.UserMapperImpl;
import com.zxz.domain.Room;
import com.zxz.domain.User;
import com.zxz.rpcclient.RpcClient;
import com.zxz.util.DateUtils;

public class IndexService {

	DateService dateService1 = RpcClient.getService(DateService.class,"com.zxz.service.DateServiceImpl","101.201.115.208",1);//分区1
	DateService dateService2 = RpcClient.getService(DateService.class,"com.zxz.service.DateServiceImpl","123.57.185.23",1);//分区2
	
	
	UserMapper userMapper = new UserMapperImpl();
	OneRoomDao oneRoomDao = new OneRoomDaoImpl();
	
	
	UserScoreDao scoreDao = UserScoreDao.getInstance();//用户的成绩
	
	
	/**得到房间的集合1
	 * @return
	 */
	public List<Room> getRoomList1(){
		String roomJSONObject = dateService1.getRoomJSONObject();
		System.out.println(roomJSONObject);
		JSONArray jsonArray = new JSONArray(roomJSONObject);
		List<Room> list = new ArrayList<>();
		for(int i=0;i<jsonArray.length();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Room room = new Room();
			room.setZhama(jsonObject.getInt("zhama"));
			User createUser = new User();
			createUser.setId(jsonObject.getInt("createUserId"));
			createUser.setUserName(jsonObject.getString("userName"));
			room.setCreateUser(createUser);
			room.setCreateDate(DateUtils.stringToDate(jsonObject.getString("createDate"), "yyyy/MM/dd HH:mm:ss"));
			room.setTotal(jsonObject.getInt("total"));
			room.setRoomNumber(jsonObject.getInt("roomNumber"));
			list.add(room);
		}
		return list;
	}
	
	/**得到房间的集合1
	 * @return
	 */
	public List<Room> getRoomList2(){
		String roomJSONObject = dateService2.getRoomJSONObject();
		System.out.println(roomJSONObject);
		JSONArray jsonArray = new JSONArray(roomJSONObject);
		List<Room> list = new ArrayList<>();
		for(int i=0;i<jsonArray.length();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Room room = new Room();
			room.setZhama(jsonObject.getInt("zhama"));
			User createUser = new User();
			createUser.setId(jsonObject.getInt("createUserId"));
			createUser.setUserName(jsonObject.getString("userName"));
			room.setCreateUser(createUser);
			room.setCreateDate(DateUtils.stringToDate(jsonObject.getString("createDate"), "yyyy/MM/dd HH:mm:ss"));
			room.setTotal(jsonObject.getInt("total"));
			room.setRoomNumber(jsonObject.getInt("roomNumber"));
			list.add(room);
		}
		return list;
	}
	
	
	/**得到房间的假数据
	 * @return
	 */
	public List<Room> getFalseRoomList(){
		Random  rand = new Random();
		Map<Integer,Integer> roomIdMap = new HashMap<>();
		Map<String, String> createUserMap = new HashMap<>();
		int total = roomIdMap.size();
		List<Room> rooms = new ArrayList<Room>();
		int totalSize = 20+rand.nextInt(10);
		while(total<totalSize){
			int nextInt = rand.nextInt(2747)+653;
			if(!roomIdMap.containsKey(nextInt)){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", nextInt);
				List<Map<String, Object>> listMap = scoreDao.queryUserScoreById(map);
				Map<String, Object> dataMap = listMap.get(0);
				Object oRoomNumber = dataMap.get("roomNumber");
				Object oNickName = dataMap.get("nickName");
				
				if(!createUserMap.containsKey(oNickName+"")){
					Room room = new Room();
					room.setRoomNumber(Integer.parseInt(oRoomNumber+""));
					User createUser = new User();
					createUser.setNickName(oNickName+"");
					createUser.setUserName(oNickName+"");
					Date nowDate = new Date();
					room.setCreateDate(nowDate);
					room.setTotal(8);
					int nextInt2 = rand.nextInt(2);
					if(nextInt2==0){
						room.setZhama(4);
					}else if(nextInt2==1){
						room.setZhama(6);
					}else{
						room.setZhama(2);
					}
					room.setCreateUser(createUser);
					rooms.add(room);
					createUserMap.put(oNickName+"", oNickName+"");
					roomIdMap.put(nextInt, nextInt);
				}
				
			}
			total = roomIdMap.size();
		}
		return rooms;
	}
	
	
	
	/**总的在线用户,和服务器建立连接的用户数 
	 * @return
	 */
	public int getTotalOneLineUser1(){
		return dateService1.getTotalOneLineUser();
	}
	
	/**总的在线用户,和服务器建立连接的用户数 
	 * @return
	 */
	public int getTotalOneLineUser2(){
		return dateService2.getTotalOneLineUser();
	}
	
	/**总的登录用户
	 * @return
	 */
	public int getTotalLoginLineUser1(){
		return dateService1.getTotalLoginLineUser();
	}
	
	/**总的登录用户
	 * @return
	 */
	public int getTotalLoginLineUser2(){
		return dateService2.getTotalLoginLineUser();
	}

	public static void main(String[] args) {
		IndexService indexService = new IndexService();
		int totalLoginLineUser = indexService.getTotalLoginLineUser1();
		System.out.println(totalLoginLineUser);
	}
	
	
	/**今日注册数
	 * @return
	 */
	public int getTodayTotalRegist() {
		Map<String, Object> map = new HashMap<String, Object>();
		String beginDate = DateUtils.getFormatDate(new Date(), "yyyy-MM-dd");
		Date tommorrow = DateUtils.getTommorrow();
		String endDate = DateUtils.getFormatDate(tommorrow, "yyyy-MM-dd");
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		return userMapper.selectUserListTotal(map );
	}

	/**得到今日房卡消耗数
	 * @return
	 */
	public int getTodayConsumeTotal() {
		Map<String, Object> map = new HashMap<String, Object>();
		String beginDate = DateUtils.getFormatDate(new Date(), "yyyy-MM-dd");
		Date tommorrow = DateUtils.getTommorrow();
		String endDate = DateUtils.getFormatDate(tommorrow, "yyyy-MM-dd");
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		return oneRoomDao.selectOneRoomListTotal(map);
	}
}
