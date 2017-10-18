package com.zxz.dao.impl;

import java.util.Date;

import com.zxz.dao.BaseDao;
import com.zxz.dao.PdkcardDao;
import com.zxz.domain.Pdkcard;

public class PdkcardDaoImpl extends BaseDao<Pdkcard> implements PdkcardDao{
	
	public static void main(String[] args) {
		PdkcardDao pdkcardDao = new PdkcardDaoImpl();
		Pdkcard record = new Pdkcard();
		record.setUserid(10020);
		record.setRefreshToken(1111+"");
		pdkcardDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int deleteByPrimaryKey(Integer userid) {
		return 0;
	}

	@Override
	public int insert(Pdkcard record) {
		record.setCreatedate(new Date());
		return super.insert("Pdkcard.insert", record);
	}

	@Override
	public int insertSelective(Pdkcard record) {
		return 0;
	}

	@Override
	public Pdkcard selectByPrimaryKey(Integer userid) {
		return (Pdkcard) super.queryForObject("Pdkcard.selectByPrimaryKey",userid);
	}

	@Override
	public int updateByPrimaryKeySelective(Pdkcard record) {
		return super.update("Pdkcard.updateByPrimaryKeySelective", record);
	}

	@Override
	public int updateByPrimaryKey(Pdkcard record) {
		return 0;
	}

	

}
