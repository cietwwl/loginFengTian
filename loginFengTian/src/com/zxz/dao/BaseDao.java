package com.zxz.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import com.mysql.jdbc.log.Log;
import com.zxz.daoTest.TestUserDao;

public class BaseDao<T> {
	private static Logger logger = Logger.getLogger(BaseDao.class);
	SqlSessionFactory ssf;
	
	public BaseDao() {
		ssf = SqlSessionFactoryBuilderManager.getSqlSessionFactory();
	}
	
	public int insert(String arg,Object object){
		SqlSession session = ssf.openSession();
		int id;
		try {
			id = session.insert(arg, object);
			session.commit();
		} finally {
			closeSession(session);
		}
		return id;
	}
	
	
	public int update(String arg,Object object){
		SqlSession session = ssf.openSession();
		int id = 0;
		try {
			id = session.update(arg, object);
			session.commit();
		} finally {
			closeSession(session);
			logger.debug("关闭连接");
		}
		return id;
	}
	
	public Object queryForObject(String arg,Object object){
		SqlSession session = ssf.openSession();
		T selectOne;
		try {
			selectOne = session.selectOne(arg, object);
			session.commit();
		} finally {
			closeSession(session);
			logger.debug("关闭连接");
		}
		return selectOne;
	}
	
	
	public List queryForList(String arg,Object t){
		SqlSession session = ssf.openSession();
		List<T> selectList;
		try {
			selectList = session.selectList(arg, t);
			session.commit();
		} finally {
			closeSession(session);
			logger.debug("关闭连接");
		}
		return selectList;
	}
	
	public static void closeSession(SqlSession session){
		session.close();
	}
	
	public static void commitSession(SqlSession session){
		session.commit();
	}
}
