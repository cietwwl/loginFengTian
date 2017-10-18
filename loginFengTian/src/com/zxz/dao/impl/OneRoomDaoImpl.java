package com.zxz.dao.impl;

import java.util.List;
import java.util.Map;

import com.zxz.dao.BaseDao;
import com.zxz.dao.OneRoomDao;
import com.zxz.domain.OneRoom;

public class OneRoomDaoImpl extends BaseDao<OneRoom> implements OneRoomDao {

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return 0;
	}

	@Override
	public int insert(OneRoom record) {
		return 0;
	}

	@Override
	public int insertSelective(OneRoom record) {
		return 0;
	}

	@Override
	public OneRoom selectByPrimaryKey(Integer id) {
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(OneRoom record) {
		return 0;
	}

	@Override
	public int updateByPrimaryKey(OneRoom record) {
		return 0;
	}

	@Override
	public int selectOneRoomListTotal(Map<String, Object> map) {
		return (int) super.queryForObject("OneRoom.selectOneRoomListTotal", map);
	}

	@Override
	public List<Map<String, Object>> findRoomListMap(Map<String, Object> map) {
		return super.queryForList("OneRoom.selectRoomListMap", map);
	}

	@Override
	public int findRoomListMapTotal(Map<String, Object> map) {
		return (int) super.queryForObject("OneRoom.selectRoomListMapTotal", map);
	}

	@Override
	public int findAllOperateChargeTotal(Map<String, Object> map) {
		return (int) super.queryForObject("OneRoom.findAllOperateChargeTotal", map);
	}

	@Override
	public int findAllOperateRemainCardTotal(Map<String, Object> map) {
		return (int) super.queryForObject("OneRoom.findAllOperateRemainCardTotal", map);
	}

	@Override
	public int findAllOperateSendCardTotal(Map<String, Object> map) {
		return (int) super.queryForObject("OneRoom.findAllOperateSendCardTotal", map);
	}

	@Override
	public int findAllUserCardTotal(Map<String, Object> map) {
		return (int) super.queryForObject("OneRoom.findAllUserCardTotal", map);
	}

	@Override
	public int findAllOperateConsumeTotal(Map<String, Object> map) {
		return (int) super.queryForObject("OneRoom.findAllOperateConsumeTotal", map);
	}

}
