package com.zxz.dao.impl;

import java.util.Date;
import java.util.Map;

import com.zxz.dao.BaseDao;
import com.zxz.dao.ZjcardDao;
import com.zxz.domain.Zjcard;

public class ZjcardDaoImpl extends BaseDao<Zjcard> implements ZjcardDao{

	
	
	public static void main(String[] args) {
		ZjcardDaoImpl daoImpl = new ZjcardDaoImpl();
//		Zjcard selectByPrimaryKey = daoImpl.selectByPrimaryKey(10020);
//		System.out.println(selectByPrimaryKey);
		Zjcard zjcard = new Zjcard();
		zjcard.setRoomcard(10);
		zjcard.setCreatedate(new Date());
		zjcard.setUserid(10021);
		int insert = daoImpl.insert(zjcard);
		System.out.println(zjcard.getUserid());
	}
	
	@Override
	public int deleteByPrimaryKey(Integer userid) {
		return 0;
	}

	@Override
	public int insert(Zjcard record) {
		record.setCreatedate(new Date());
		return super.insert("Zjcard.insert", record);
	}

	@Override
	public int insertSelective(Zjcard record) {
		return 0;
	}

	@Override
	public Zjcard selectByPrimaryKey(Integer userid) {
		return (Zjcard) super.queryForObject("Zjcard.selectByPrimaryKey",userid);
	}

	@Override
	public int updateByPrimaryKeySelective(Zjcard record) {
		return super.update("Zjcard.updateByPrimaryKeySelective", record);
	}

	@Override
	public int updateByPrimaryKey(Zjcard record) {
		return 0;
	}

	@Override
	public Map<String, Object> getTimeElse(Map<String, Object> map) {
		Object queryForObject = super.queryForObject("Zjcard.getTimeElse", map);
		return (Map<String, Object>) queryForObject;
	}

	@Override
	public int fenxiangchenggong(Map<String, Object> map) {
		return (int)super.queryForObject("Zjcard.fenxiangchenggong", map);
	}
}
