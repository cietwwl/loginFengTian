package com.zxz.dao;

import java.util.List;
import java.util.Map;

import com.zxz.domain.OperateChargeRecord;

public interface OperateChargeRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OperateChargeRecord record);

    int insertSelective(OperateChargeRecord record);

    OperateChargeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OperateChargeRecord record);

    int updateByPrimaryKey(OperateChargeRecord record);
    
    
    /**管理员查询，充值记录 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectUserRechargeRecordByManager(Map<String, Object> map);

	/**管理员查询充值总数
	 * @param map
	 * @return
	 */
	int selectUserRechargeRecordCountByManager(Map<String, Object> map);

	/**代理查询自己的充值记录
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> findChargeRecordByOperate(Map<String, Object> map);

	/**代理查询充值记录总数
	 * @param map
	 * @return
	 */
	int findChargeRecordByOperateTotal(Map<String, Object> map);
	
	
	/**管理员查看充值记录 
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> selectChargeRecordByAdmin(Map<String, Object> map);
	
	/**管理员查看充值记录 总数
	 * @param map
	 * @return
	 */
	int selectChargeRecordByAdminCount(Map<String, Object> map);
	
	
	
}