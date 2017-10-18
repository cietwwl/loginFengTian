package com.zxz.dao.impl;

import com.zxz.dao.BaseDao;
import com.zxz.dao.VedioDao;
import com.zxz.domain.Vedio;

public class VedioDaoImpl extends BaseDao<Vedio> implements VedioDao{

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return 0;
	}

	@Override
	public int insert(Vedio record) {
		return 0;
	}

	@Override
	public int insertSelective(Vedio record) {
		return 0;
	}

	@Override
	public Vedio selectByPrimaryKey(Integer id) {
		return (Vedio) super.queryForObject("Vedio.selectByPrimaryKey",id);
	}
	public static void main(String[] args) {
		VedioDaoImpl vedioDaoImpl = new VedioDaoImpl();
		Vedio selectByPrimaryKey = vedioDaoImpl.selectByPrimaryKey(575852);
		System.out.println(selectByPrimaryKey);
	}
	@Override
	public int updateByPrimaryKeySelective(Vedio record) {
		return 0;
	}

	@Override
	public int updateByPrimaryKey(Vedio record) {
		return 0;
	}

}
