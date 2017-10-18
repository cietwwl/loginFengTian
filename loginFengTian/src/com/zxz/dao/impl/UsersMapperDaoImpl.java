package com.zxz.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zxz.dao.BaseDao;
import com.zxz.domain.UsersMapper;

public class UsersMapperDaoImpl extends BaseDao<UsersMapper>{

	public UsersMapper findUsersMapperByCondition(UsersMapper usersMapper) {
		return (UsersMapper) super.queryForObject("UsersMapper.selectByCondition", usersMapper);
	}
	public static void main(String[] args) {

		UsersMapperDaoImpl u = new UsersMapperDaoImpl();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", 91438);
		Map<String,Object> queryMyClub = u.queryMyClub(map);
		System.out.println(queryMyClub);
	}
	
	public List<UsersMapper> queryForList(UsersMapper usersMapper) {
		return super.queryForList("UsersMapper.selectByCondition", usersMapper);
	}
	
	
	public Map<String, Object> queryMyClub(Map<String, Object> map){
		Object queryForObject = super.queryForObject("UsersMapper.queryMyClub", map);
		return (Map<String, Object>) queryForObject;
	}
	
	
	
	public int insert(UsersMapper usersMapper){
		int insert = super.insert("UsersMapper.insert", usersMapper);
		return insert;
	}
	
	
	
	public int addUserCardByBangDingJuLeBu(Map<String, Object> map){
		int insert = (int)super.queryForObject("UsersMapper.addUserCardByBangDingJuLeBu", map);
		return insert;
	}
	
}
