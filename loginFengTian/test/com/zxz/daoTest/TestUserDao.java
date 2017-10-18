package com.zxz.daoTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import com.zxz.dao.UserMapper;
import com.zxz.dao.impl.UserMapperImpl;
import com.zxz.domain.User;

public class TestUserDao {
	private static Logger logger = Logger.getLogger(TestUserDao.class);
//	static SqlSessionFactory ssf;
//
//	static {
//
//		// 1
//		SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
//		// 2
//		ssf = ssfb.build(TestUserDao.class.getClassLoader().getResourceAsStream("config/SqlMapConfig.xml"));
//
//	}

	public static void main(String[] args) {

//		testSelect();
//		testSelectById();
		
		UserMapper userMapper =  new UserMapperImpl();
		Map map = new HashMap();
		map.put("rowStart", 0);
		map.put("pageSize", 10);
		List<User> userList = userMapper.selectUserList(map );
		for(User user:userList){
			System.out.println(user.getNickName());
		}
		System.out.println(userList.size());
		
	}

	private static void testSelectById() {
		UserMapper userMapper =  new UserMapperImpl();
		User user = userMapper.selectByPrimaryKey(31);
		System.out.println(user.getNickName());
	}

	private static void testSelect() {
//		SqlSession session = ssf.openSession();
//		UserMapper userMapper = session.getMapper(UserMapper.class);
//		User user = userMapper.selectByPrimaryKey(31);
//		System.out.println(user.getNickname());
//		session.close();
//		logger.debug("ddd");
	}

	
	
}
