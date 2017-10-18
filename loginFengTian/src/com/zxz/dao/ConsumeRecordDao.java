package com.zxz.dao;

import java.util.List;
import java.util.Map;

import com.zxz.domain.ConsumeRecord;

public interface ConsumeRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsumeRecord record);

    int insertSelective(ConsumeRecord record);

    ConsumeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsumeRecord record);

    int updateByPrimaryKey(ConsumeRecord record);

	/**给用户充值房卡
	 * @param record
	 */
	int addUserCardByUserId(ConsumeRecord record);
	
	
	/**查询用户的充值记录
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> selectUserRechargeRecord(Map<String,Object> map);
	
	/**查询用户的充值记录总数
	 * @param map
	 * @return
	 */
	int selectUserRechargeRecordCount(Map<String,Object> map);

	
	
}