package com.zxz.dao;

import java.util.List;
import java.util.Map;

import com.zxz.domain.OneRoom;

public interface OneRoomDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OneRoom record);

    int insertSelective(OneRoom record);

    OneRoom selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OneRoom record);

    int updateByPrimaryKey(OneRoom record);
    
    
    /**根据条件，查询总数
     * @return
     */
    int selectOneRoomListTotal(Map<String, Object> map);

	/**查询集合
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> findRoomListMap(Map<String, Object> map);

	/**查询出总数
	 * @param map
	 * @return
	 */
	int findRoomListMapTotal(Map<String, Object> map);

	/**代理充值总数
	 * @return
	 */
	int findAllOperateChargeTotal(Map<String, Object> map);

	/**代理余卡总数
	 * @return
	 */
	int findAllOperateRemainCardTotal(Map<String, Object> map);

	/**代理赠送总数
	 * @return
	 */
	int findAllOperateSendCardTotal(Map<String, Object> map);

	/**用户的余卡总数
	 * @return
	 */
	int findAllUserCardTotal(Map<String, Object> map);

	/**代理消耗总数 
	 * @return
	 */
	int findAllOperateConsumeTotal(Map<String, Object> map);

    
}