package com.zxz.service;

import com.zxz.dao.OperateUserDao;
import com.zxz.dao.impl.OperateUserDaoImpl;
import com.zxz.domain.OperateUser;

public class OperaterUserService {

	OperateUserDao operateUserDao = new OperateUserDaoImpl();
	public OperaterUserService() {
	}

	public OperateUser findOperateUserById(Integer id) {
		OperateUser operateUser = new OperateUser();
		operateUser.setId(id);
		return operateUserDao.selectOperateUserSelective(operateUser).get(0);
	}
}
