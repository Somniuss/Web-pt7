package com.somniuss.web.service.impl;

import com.somniuss.web.bean.User;
import com.somniuss.web.dao.DaoException;
import com.somniuss.web.dao.DaoProvider;
import com.somniuss.web.dao.UserDao;
import com.somniuss.web.service.ServiceException;
import com.somniuss.web.service.UserService;

public class UserServiceImpl implements UserService {

	private final UserDao userDao = DaoProvider.getInstance().getUserDao();

	@Override
	public User signIn(String name, String password) throws ServiceException {
	    try {
	        User user = userDao.authorization(name, password);
	        return user;
	    } catch (DaoException e) {
	        throw new ServiceException("Ошибка при аутентификации пользователя", e);
	    }
	}



	@Override
	public User registration(String name, String email, String password) throws ServiceException {
	    try {
	        
	      
	        // Создаем нового пользователя
	        User user = new User(name, email, password);

	        // Добавляем пользователя в базу данных через UserDao
	        return userDao.registration(name, email, password);
	    } catch (DaoException e) {
	        throw new ServiceException("Ошибка при регистрации пользователя", e);
	    }
	}

	

}
