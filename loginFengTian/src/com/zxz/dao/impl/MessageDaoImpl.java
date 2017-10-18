package com.zxz.dao.impl;

import java.util.List;
import java.util.Map;

import com.zxz.dao.BaseDao;
import com.zxz.dao.MessageDao;
import com.zxz.domain.Message;

public class MessageDaoImpl extends BaseDao<Message> implements MessageDao{

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return 0;
	}

	@Override
	public int insert(Message record) {
		return super.insert("Message.insert", record);
	}

	@Override
	public int insertSelective(Message record) {
		return 0;
	}

	@Override
	public Message selectByPrimaryKey(Integer id) {
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(Message record) {
		return 0;
	}

	@Override
	public int updateByPrimaryKey(Message record) {
		return 0;
	}

	@Override
	public List<Map<String, Object>> selectListByMap(Map<String, Object> map) {
		return super.queryForList("Message.selectListByMap", map);
	}

	@Override
	public int selectListByMapTotal(Map<String, Object> map) {
		return (int) super.queryForObject("Message.selectListByMapTotal", map);
	}

}
