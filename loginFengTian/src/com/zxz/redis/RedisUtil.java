package com.zxz.redis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import config.Constants;

import redis.clients.jedis.Jedis;

public class RedisUtil {

	
	private static Logger logger = Logger.getLogger(RedisUtil.class);
	
	private static ResourceBundle resource = ResourceBundle.getBundle("config/redisconfig");
	
	private static final String HOST = resource.getString("host").trim();
	
	private static final String PASSWORD = resource.getString("redispwd").trim();
	
	private static final int PORT = Integer.parseInt(resource.getString("port").trim());
	
	public static void main(String[] args) {
		Map<String, String> hashMap = getHashMap("uid"+10021, Constants.REDIS_DB_SANGUO);
		System.out.println(hashMap);
	}

	
	/**从key得到value
	 * @param key
	 * @return
	 */
	public static String getKey(String key){
		String result = null;
		Jedis jedis = MyJedisPool.getJedis();
		try {
			result = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal(e);
		} finally {
			MyJedisPool.returnResource(jedis);
		}
		return result;
	}
	
	/**从key得到value
	 * @param key
	 * @return
	 */
	public static String getKey(String key,int db){
		String result = null;
		Jedis jedis = MyJedisPool.getJedis();
		try {
			jedis.select(db);
			result = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal(e);
		} finally {
			MyJedisPool.returnResource(jedis);
		}
		return result;
	}
	
	

	
	
	/**从key得到value
	 * @param key
	 * @return
	 */
	public static Long delKey(String key,int db){
		Long del = null;
		Jedis jedis = MyJedisPool.getJedis();
		try {
			jedis.select(db);
			del = jedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal(e);
		}finally{
			MyJedisPool.returnResource(jedis);
		}
		return del;
	}
	
	
	

	
	
	/**从key得到value
	 * @param key
	 * @return
	 */
	public static String setKey(String key,String value,int db){
		String result = null;
		Jedis jedis = MyJedisPool.getJedis();
		try {
			jedis.select(db);
			result = jedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal(e);
		}finally {
			MyJedisPool.returnResource(jedis);
		}
		return result;
	}
	
	
	
	/**得到一个jedis
	 * @param key
	 * @return
	 */
	private static Jedis getOneRedis(){
		try {
			Jedis jedis = MyJedisPool.getJedis();
			return jedis;
		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal(e);
		}
		return null;
	}
	
	
	public static String setHashMap(String key,HashMap<String , String> hashMap){
		Jedis oneRedis = getOneRedis();
		String hmset;
		try {
			hmset = oneRedis.hmset(key, hashMap);
		} finally {
			MyJedisPool.returnResource(oneRedis);
		}
		return hmset;
	}
	
	
	
	
	/**
	 * @param key
	 * @param hashMap
	 * @param db  分区
	 * @return
	 */
	public static String setHashMap(String key,HashMap<String , String> hashMap,int db,int expireTime){
		Jedis oneRedis = getOneRedis();
		String hmset;
		try {
			oneRedis.select(db);
			hmset = oneRedis.hmset(key, hashMap);
			oneRedis.expire(key, expireTime);
		} finally {
			MyJedisPool.returnResource(oneRedis);
		}
		return hmset;
	}
	
	
	
	
	public static Map<String,String> getHashMap(String key,int db){
		Map<String,String> map;
		Jedis oneRedis = getOneRedis();
		oneRedis.select(db);
		try {
			map = oneRedis.hgetAll(key);
		} finally {
			MyJedisPool.returnResource(oneRedis);
		}
		return map;
	}

}