package com.somniuss.web.logic;

import com.somniuss.web.bean.AuthInfo;
import com.somniuss.web.bean.User;
import com.somniuss.web.dao.DaoException;
import com.somniuss.web.dao.impl.SQLUserDao;

public class LogicStub {
    
	public User checkAuth(AuthInfo authInfo) {
		
		SQLUserDao userDao = new SQLUserDao();
	    try {
	        return userDao.authorization(authInfo.getLogin(), authInfo.getPassword());
	    } catch (DaoException e) {
	        System.err.println("Ошибка при проверке аутентификации: " + e.getMessage());
	        return null;
	    }
	}

}

