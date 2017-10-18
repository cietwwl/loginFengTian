package com.zxz.dao.impl;

import java.util.List;
import java.util.Map;

import com.zxz.dao.BaseDao;
import com.zxz.dao.OperateUserDao;
import com.zxz.domain.ConsumeRecord;
import com.zxz.domain.OperateUser;

public class OperateUserDaoImpl extends BaseDao<OperateUser> implements OperateUserDao{

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return 0;
	}

	@Override
	public int insert(OperateUser record) {
		 return super.insert("OperateUser.insert", record);
	}

	@Override
	public int insertSelective(OperateUser record) {
		return 0;
	}

	@Override
	public OperateUser selectByPrimaryKey(Integer id) {
		return (OperateUser) super.queryForObject("OperateUser.selectByPrimaryKey", id);
	}

	@Override
	public int updateByPrimaryKeySelective(OperateUser record) {
		return super.update("OperateUser.updateByPrimaryKeySelective", record);
	}

	@Override
	public int updateByPrimaryKey(OperateUser record) {
		return 0;
	}

	@Override
	public List<OperateUser> selectListByMap(Map<String, Object> map) {
		return super.queryForList("OperateUser.selectListByMap", map);
	}

	@Override
	public List<OperateUser> selectOperateUserSelective(OperateUser operateUser) {
		return super.queryForList("OperateUser.selectOperateUserSelective", operateUser);
	}

	@Override
	public int selectUserListTotal(Map<String, Object> map) {
		return (int) super.queryForObject("OperateUser.selectOperateUserListTotal", map);
	}

	@Override
	public List<Map<String, Object>> selectOperateUserList(
			Map<String, Object> map) {
		return super.queryForList("OperateUser.selectOperateUserList", map);
	}

	@Override
	public int selectOneRecommonId() {
		return (int) super.queryForObject("OperateUser.selectOneRecommonId", null);
	}
	
	
	@Override
	public int updateOneRecommonIdUsed(int recommendId) {
		 Object result = super.update("OperateUser.updateOneRecommonIdUsed", recommendId);
		 return (int) (result);
	}

	@Override
	public int chargeCard(ConsumeRecord record) {
		return (int)super.queryForObject("OperateUser.chargeCard", record);
	}

	
	
}
