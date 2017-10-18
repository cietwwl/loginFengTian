package com.zxz.dao;

import java.util.List;
import java.util.Map;

import com.zxz.domain.User;

public interface UserMapper{
	
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    
    /**查询用户
     * @param map
     * @return
     */
    List<User> selectUserList(Map map);

	/**查询符合条件的总数
	 * @param map
	 * @return
	 */
	int selectUserListTotal(Map<String, Object> map);

	/**给用户充值
	 * @param map
	 * @return
	 */
	int updateUserCard(Map<String, Object> map);

	/**
	 * @param roomNumber
	 * @return
	 */
	int disbanRoom(String roomNumber);
	
	
	/**用户登录
	 * @param user
	 * @return
	 */
	public User findUser(User user);
    
	
	/**修改用户信息
	 * @param user
	 * @return
	 */
	public int modifyUser(User user);

	Map<Object, Object> findZjUserByUserId(int userId);

	Map<Object, Object> findAgentOrder(Map<Object, Object> hashMap);
}