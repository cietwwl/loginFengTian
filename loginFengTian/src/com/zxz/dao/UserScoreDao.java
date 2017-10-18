package com.zxz.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zxz.domain.UserScore;

public class UserScoreDao extends BaseDao<UserScore>{

	static UserScoreDao userScoreDao;
	
	private UserScoreDao() {
	}
	
	public static UserScoreDao getInstance(){
		if(userScoreDao!=null){
			return userScoreDao;
		}else{
			synchronized (UserScoreDao.class) {
				userScoreDao = new UserScoreDao();
				return userScoreDao;
			}
		}
	}
	
	/**保存用户
	 * @param user
	 * @return
	 */
	public int saveUserScore(UserScore userScore) {
		int id = super.insert("UserScore.save", userScore);
		return id;
	}
	
	
	/**查询用户的成绩 
	 * @param map
	 * @return
	 */
	public List<UserScore> findUserScore(Map<String, Object> map){
		
		List<UserScore> list = super.queryForList("UserScore.queryForMap", map);
		
		return list;
		
	}
	public static void main(String[] args) {
		UserScoreDao userScoreDao2 = new UserScoreDao();
		Map<String, Object> map = new HashMap<>();
		map.put("sumScoreId", 101110);
		map.put("userid", 10021);
		List<UserScore> findUserScore = userScoreDao2.findUserScore(map);
		System.out.println(findUserScore.size());
		System.out.println(findUserScore);
	}
	/**查询用户的成绩 
	 * @param map
	 * @return
	 */
	public List<UserScore> findUserScorePdk(Map<String, Object> map){
		List<UserScore> list = super.queryForList("UserScore.queryForMapPdk", map);
		return list;
	}
	
	
	/**查询用户的成绩 
	 * @param map
	 * @return
	 */
	public int findUserScoreTotal(Map<String, Object> map){
		int total =(int) super.queryForObject("UserScore.queryForMapCount", map);
		return total;
		
	}
	
	/**查询用户的成绩 
	 * @param map
	 * @return
	 */
	public int findUserScoreTotalPdk(Map<String, Object> map){
		int total =(int) super.queryForObject("UserScore.queryForMapCountPdk", map);
		return total;
		
	}
	
	
	/**查询用户的成绩 
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryUserScoreById(Map<String, Object> map){
		List<Map<String,Object>> list = super.queryForList("UserScore.queryUserScoreById", map);
		return list;
	}
	
	
	/**查询用户的成绩 jia
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> findUserScoreByRoomId(Map<String, Object> map){
		List<Map<String,Object>> list = super.queryForList("UserScore.findUserScoreByRoomId", map);
		return list;
	}
	
	/**查询用户的成绩 
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> findUserSumScore(Map<String, Object> map){
		List<Map<String,Object>> queryForList = super.queryForList("UserScore.querySumScore", map);
		return queryForList;
	}
	
	/**查询用户的成绩 
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> findUserSumScorePdk(Map<String, Object> map){
		List<Map<String,Object>> queryForList = super.queryForList("UserScore.querySumForMapPdk", map);
		return queryForList;
	}
	
	
	/**得到用户当前的游戏中的分数
	 * @param user
	 * @return
	 */
	public int selectUserScoreByCurrentRoomNumber(UserScore userScore) {
		int score = (int) super.queryForObject("UserScore.selectUserScoreByCurrentRoomNumber", userScore);
		return score;
	}
	
}
