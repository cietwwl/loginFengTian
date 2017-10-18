package com.zxz.dao;

import java.util.Map;

import com.zxz.domain.Zjcard;

public interface ZjcardDao {
    int deleteByPrimaryKey(Integer userid);

    int insert(Zjcard record);

    int insertSelective(Zjcard record);

    Zjcard selectByPrimaryKey(Integer userid);

    int updateByPrimaryKeySelective(Zjcard record);

    int updateByPrimaryKey(Zjcard record);

	Map<String, Object> getTimeElse(Map<String, Object> map);

	int fenxiangchenggong(Map<String, Object> map);
}