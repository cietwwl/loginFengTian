package com.zxz.dao;

import java.util.List;
import java.util.Map;

import com.zxz.domain.AppVersion;

public interface AppVersionDao {
	
    int deleteByPrimaryKey(Integer id);

    int insert(AppVersion record);

    int insertSelective(AppVersion record);

    AppVersion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppVersion record);

    int updateByPrimaryKey(AppVersion record);
    
    /**查询版本的列表
     * @param map
     * @return
     */
    public List<AppVersion> selectListByMap(Map<String, Object> map);
    
}