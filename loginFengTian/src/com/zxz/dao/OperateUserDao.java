package com.zxz.dao;

import java.util.List;
import java.util.Map;

import com.zxz.domain.ConsumeRecord;
import com.zxz.domain.OperateUser;

public interface OperateUserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OperateUser record);

    int insertSelective(OperateUser record);

    OperateUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OperateUser record);

    int updateByPrimaryKey(OperateUser record);
    
    /**查询集合
     * @param map
     * @return
     */
    List<OperateUser> selectListByMap(Map<String,Object> map);

	/**根据条件查询出集合
	 * @param operateUser
	 * @return
	 */
	List<OperateUser> selectOperateUserSelective(OperateUser operateUser);

	/**根据条件查询总数
	 * @param map
	 * @return
	 */
	int selectUserListTotal(Map<String, Object> map);

	/**根据条件查询出列表
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectOperateUserList(Map<String, Object> map);

	/**查询出一个推荐号
	 * @return
	 */
	int selectOneRecommonId();

	/**使推荐号使用
	 * @param recommendId
	 * @return
	 */
	int updateOneRecommonIdUsed(int recommendId);

	/**代理充值
	 * @param record
	 * @return 代理充值后的房卡数
	 */
	int chargeCard(ConsumeRecord record);
    
}