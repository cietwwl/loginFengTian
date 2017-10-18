package com.zxz.dao.impl;

import java.util.List;
import java.util.Map;

import com.zxz.dao.AppVersionDao;
import com.zxz.dao.BaseDao;
import com.zxz.domain.AppVersion;

public class AppVersionDaoImpl extends BaseDao<AppVersion> implements AppVersionDao{

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return 0;
	}

	@Override
	public int insert(AppVersion record) {
		return 0;
	}

	@Override
	public int insertSelective(AppVersion record) {
		return 0;
	}

	@Override
	public AppVersion selectByPrimaryKey(Integer id) {
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(AppVersion record) {
		return 0;
	}

	@Override
	public int updateByPrimaryKey(AppVersion record) {
		return 0;
	}

	@Override
	public List<AppVersion> selectListByMap(Map<String, Object> map) {
		
		return super.queryForList("AppVersion.selectListByMap", map);
		
	}

}
