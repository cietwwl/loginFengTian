package com.zxz.dao;

import com.zxz.domain.Vedio;

public interface VedioDao {
	
	int deleteByPrimaryKey(Integer id);

	int insert(Vedio record);

	int insertSelective(Vedio record);

	Vedio selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Vedio record);

	int updateByPrimaryKey(Vedio record);
	
}