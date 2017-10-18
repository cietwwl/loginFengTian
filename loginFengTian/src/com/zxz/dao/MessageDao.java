package com.zxz.dao;

import java.util.List;
import java.util.Map;

import com.zxz.domain.Message;

public interface MessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

	List<Map<String, Object>> selectListByMap(Map<String,Object> map);

	/**查询总数
	 * @param map
	 * @return
	 */
	int selectListByMapTotal(Map<String, Object> map);
}