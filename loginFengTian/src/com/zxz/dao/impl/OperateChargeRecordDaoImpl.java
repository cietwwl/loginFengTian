package com.zxz.dao.impl;

import java.util.List;
import java.util.Map;

import com.zxz.dao.BaseDao;
import com.zxz.dao.ConsumeRecordDao;
import com.zxz.dao.OperateChargeRecordDao;
import com.zxz.domain.ConsumeRecord;
import com.zxz.domain.OperateChargeRecord;

public class OperateChargeRecordDaoImpl extends BaseDao<OperateChargeRecord> implements OperateChargeRecordDao {

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return 0;
	}

	@Override
	public int insert(OperateChargeRecord record) {
		return 0;
	}

	@Override
	public int insertSelective(OperateChargeRecord record) {
		return 0;
	}

	@Override
	public OperateChargeRecord selectByPrimaryKey(Integer id) {
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(OperateChargeRecord record) {
		return 0;
	}

	@Override
	public int updateByPrimaryKey(OperateChargeRecord record) {
		return 0;
	}

	@Override
	public List<Map<String, Object>> selectUserRechargeRecordByManager(
			Map<String, Object> map) {
		return super.queryForList("OperateChargeRecord.selectUserRechargeRecord", map);
	}

	@Override
	public int selectUserRechargeRecordCountByManager(Map<String, Object> map) {
		return (int) super.queryForObject("OperateChargeRecord.selectUserRechargeRecordCount", map);
	}

	@Override
	public List<Map<String, Object>> findChargeRecordByOperate(
			Map<String, Object> map) {
		return super.queryForList("OperateChargeRecord.selectOperateChareRecord", map);
	}

	@Override
	public int findChargeRecordByOperateTotal(Map<String, Object> map) {
		return (int) super.queryForObject("OperateChargeRecord.selectOperateChareRecordTotal", map);
	}

	@Override
	public List<Map<String, Object>> selectChargeRecordByAdmin(
			Map<String, Object> map) {
		return super.queryForList("OperateChargeRecord.selectChargeRecordByAdmin", map);
	}

	@Override
	public int selectChargeRecordByAdminCount(Map<String, Object> map) {
		return (int) super.queryForObject("OperateChargeRecord.selectChargeRecordByAdminCount", map);
	}

	
}
