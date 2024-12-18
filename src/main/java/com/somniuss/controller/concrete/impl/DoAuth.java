package com.somniuss.controller.concrete.impl;

import java.io.IOException;

import com.somniuss.web.bean.AuthInfo;
import com.somniuss.web.bean.User;
import com.somniuss.web.controller.concrete.Command;
import com.somniuss.web.logic.LogicStub;
import com.somniuss.web.service.ServiceException;
import com.somniuss.web.service.impl.UserServiceImpl;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class DoAuth implements Command{
	private final LogicStub logic = new LogicStub();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String login = request.getParameter("login");
	    String password = request.getParameter("password");

	    System.out.println("Perform user authentication and authorization. Login: " + login);

	    try {
	        User user = new UserServiceImpl().signIn(login, password); // Используем сервис

	        if (user != null) {
	            HttpSession session = request.getSession(true);
	            session.setAttribute("user", user);

	            request.setAttribute("invitationMessage", "Hello, user!");
	            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/main.jsp");
	            dispatcher.forward(request, response);
	        } else {
	            request.setAttribute("authError", "Неправильный логин или пароль!");
	            RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
	            dispatcher.forward(request, response);
	        }
	    } catch (ServiceException e) {
	        e.printStackTrace(); // Логируем ошибку
	        request.setAttribute("authError", "Ошибка при аутентификации пользователя. Пожалуйста, попробуйте позже.");
	        RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
	        dispatcher.forward(request, response);
	    }
	}



}
