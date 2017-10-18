package com.zxz.dao.impl;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.zxz.dao.BaseDao;
import com.zxz.dao.UserMapper;
import com.zxz.domain.User;

public class UserMapperImpl extends BaseDao<User> implements UserMapper{

	
	public UserMapperImpl() {
	}
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return 0;
	}

	@Override
	public int insert(User record) {
		return super.insert("User.save", record);
	}

	@Override
	public int insertSelective(User record) {
		return 0;
	}

	@Override
	public User selectByPrimaryKey(Integer id) {
		User user = new User();
		user.setId(id);
		return (User) super.queryForObject("User.selectByPrimaryKey", user);
	}
	
	
	public User selectLastRegist() {
		return (User) super.queryForObject("User.selectLastRegist",null);
	}

	@Override
	public int updateByPrimaryKeySelective(User record) {
		return super.update("User.updateByPrimaryKeySelective", record);
	}

	@Override
	public int updateByPrimaryKey(User record) {
		return 0;
	}

	@Override
	public List<User> selectUserList(Map map) {
		return queryForList("User.selectUserList", map);
	}

	@Override
	public int selectUserListTotal(Map<String, Object> map) {
		return (int) queryForObject("User.selectUserListTotal", map);
	}

	@Override
	public int updateUserCard(Map<String, Object> map) {
		return update("User.updateUserCard", map);
	}

	@Test
	public void testSaveUser()
	{
		User u= new User();
		u.setUserName("hahah");
		u.setPassword("dddd");
//		User findUser = findUser(u);
		u.setRoomCard(10000);
		insert(u);
	}

	@Override
	public int disbanRoom(String roomNumber) {
		return update("User.disbanRoom", roomNumber);
	}
	
	
	public User findUser(User user) {
		return (User) super.queryForObject("User.queryForLogin", user);
	}
	
	
	/**修改用户
	 * @param user
	 * @return
	 */
	public int modifyUser(User user) {
		int count = 0;
		count = super.update("User.modify", user);
		return count;
	}

	@Override
	public Map<Object,Object> findZjUserByUserId(int id){
		return (Map<Object, Object>) super.queryForObject("User.selectZhenJiang", id);
	}

	@Override
	public Map<Object, Object> findAgentOrder(Map<Object, Object> hashMap) {
		return (Map<Object, Object>) super.queryForObject("User.findAgentOrder", hashMap);
	}
	
}
