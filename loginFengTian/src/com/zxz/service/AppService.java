package com.zxz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zxz.dao.AppVersionDao;
import com.zxz.dao.impl.AppVersionDaoImpl;
import com.zxz.domain.AppVersion;

public class AppService {

	AppVersionDao appVersionDao = new AppVersionDaoImpl();
	
	/**
	 * @return
	 */
	public AppVersion getAppVersion(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rowStart", 0);
		map.put("pageSize", 1);
		List<AppVersion> list = appVersionDao.selectListByMap(map);
		if(list.size()>0){
			AppVersion appVersion = list.get(0);
			return appVersion;
		}
		return null;
	}
	
	/**
	 * @return
	 */
	public AppVersion getAppVersionZhenJaing(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rowStart", 1);
		map.put("pageSize", 1);
		List<AppVersion> list = appVersionDao.selectListByMap(map);
		if(list.size()>0){
			AppVersion appVersion = list.get(0);
			return appVersion;
		}
		return null;
	}
	
	/**
	 * @return
	 */
	public AppVersion getAppVersionPdk(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rowStart", 2);
		map.put("pageSize", 1);
		List<AppVersion> list = appVersionDao.selectListByMap(map);
		if(list.size()>0){
			AppVersion appVersion = list.get(0);
			return appVersion;
		}
		return null;
	}
	
}
