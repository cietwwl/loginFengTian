package com.zxz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zxz.dao.MessageDao;
import com.zxz.dao.OneRoomDao;
import com.zxz.dao.UserMapper;
import com.zxz.dao.impl.MessageDaoImpl;
import com.zxz.dao.impl.OneRoomDaoImpl;
import com.zxz.dao.impl.UserMapperImpl;
import com.zxz.domain.Message;
import com.zxz.util.PageUtil;
import com.zxz.util.StringUtil;

import config.Constants;

public class RoomService implements Constants{

	OneRoomDao oneRoomDao = new OneRoomDaoImpl();
	MessageDao messageDao = new MessageDaoImpl();
	UserMapper userMapper = new UserMapperImpl();
	/**
	 * @param pageUtil
	 * @param userIdOrNick 房主id或昵称
	 * @param roomNumber 房间号
	 * @param beginDate 创建开始
	 * @param endDate 创建结束
	 * @param createDate 创建时间排序
	 * @return
	 */
	public List<Map<String, Object>> roomListMap(PageUtil<Map<String, Object>> pageUtil, String userIdOrNick, String roomNumber, String beginDate, String endDate, String createDate
			){
		Map<String,Object> map = new HashMap<String, Object>();
		if(!StringUtil.isNull(userIdOrNick)){
			boolean isNumber = StringUtil.isNumber(userIdOrNick);
			if(isNumber){
				map.put("userId", userIdOrNick);
			}else{
				map.put("nickName", userIdOrNick);
			}
		}
		if(!StringUtil.isNull(roomNumber)){
			map.put("roomNumber", roomNumber);
		}
		if(!StringUtil.isNull(beginDate)){
			map.put("beginDate", beginDate);
		}
		if(!StringUtil.isNull(endDate)){
			map.put("endDate", endDate);
		}
		if(StringUtil.isNotAllEmpty(createDate)){
			map.put(haveOrderby, true);
			map.put("createDate", createDate);
		}
		map.put("rowStart", pageUtil.getRowStart());
		map.put("pageSize", pageUtil.getSize());
		List<Map<String, Object>> list = oneRoomDao.findRoomListMap(map);
		int total = oneRoomDao.findRoomListMapTotal(map);
		pageUtil.setList(list);
		pageUtil.setTotalCount(total);
		return null;
	}
	
	/**保存消息
	 * @param message
	 */
	public void saveMessage(String smessage) {
		Message message = new Message();
		message.setMessage(smessage);
		message.setType(0);
		messageDao.insert(message);
	}

	/**得到购买的消息列表
	 * @param pageUtil
	 */
	public void buyMessageList(PageUtil<Map<String, Object>> pageUtil) {
		Map<String , Object> map = new HashMap<>();
		map.put(rowStart, pageUtil.getStartRow());
		map.put(pageSize, pageUtil.getSize());
		map.put("type", 0);
		List<Map<String,Object>> list =  messageDao.selectListByMap(map);
		int total =  messageDao.selectListByMapTotal(map);
		pageUtil.setList(list);
		pageUtil.setTotalCount(total);
	}

	/**代理充值总数
	 * @return
	 */
	public int findAllOperateChargeTotal() {
		Map<String,Object> map = new HashMap<>();
		return oneRoomDao.findAllOperateChargeTotal(map);
	}

	/**代理余卡总数
	 * @return
	 */
	public int findAllOperateRemainCardTotal() {
		Map<String,Object> map = new HashMap<>();
		return oneRoomDao.findAllOperateRemainCardTotal(map);
	}

	/**代理赠送总数
	 * @return
	 */
	public int findAllOperateSendCardTotal() {
		Map<String,Object> map = new HashMap<>();
		return oneRoomDao.findAllOperateSendCardTotal(map);
	}

	/**用户的余卡总数
	 * @return
	 */
	public int findAllUserCardTotal() {
		Map<String,Object> map = new HashMap<>();
		return oneRoomDao.findAllUserCardTotal(map);
	}

	/**代理消耗总数 
	 * @return
	 */
	public int findAllOperateConsumeTotal() {
		Map<String,Object> map = new HashMap<>();
		return oneRoomDao.findAllOperateConsumeTotal(map);
	}

	public int disbanRoom(String roomNumber) {
		return userMapper.disbanRoom(roomNumber);
	}
	
}
