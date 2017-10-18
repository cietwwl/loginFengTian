package com.zxz.dao.impl;

import java.util.List;
import java.util.Map;

import com.zxz.dao.BaseDao;
import com.zxz.dao.ConsumeRecordDao;
import com.zxz.domain.ConsumeRecord;

public class ConsumeRecordDaoImpl extends BaseDao<ConsumeRecord> implements ConsumeRecordDao{

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return 0;
	}

	@Override
	public int insert(ConsumeRecord record) {
		return super.insert("ConsumeRecord.insert", record);
	}

	@Override
	public int insertSelective(ConsumeRecord record) {
		return 0;
	}

	@Override
	public ConsumeRecord selectByPrimaryKey(Integer id) {
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(ConsumeRecord record) {
		return 0;
	}

	@Override
	public int updateByPrimaryKey(ConsumeRecord record) {
		return 0;
	}

	@Override
	public int addUserCardByUserId(ConsumeRecord record) {
		return (int) super.queryForObject("ConsumeRecord.addUserCardByUserId", record);
	}

	@Override
	public List<Map<String, Object>> selectUserRechargeRecord(Map<String, Object> map) {
		return (List) super.queryForList("ConsumeRecord.selectUserRechargeRecord", map);
	}

	@Override
	public int selectUserRechargeRecordCount(
			Map<String, Object> map) {
		return (int)super.queryForObject("ConsumeRecord.selectUserRechargeRecordCount", map);
	}

	

}
