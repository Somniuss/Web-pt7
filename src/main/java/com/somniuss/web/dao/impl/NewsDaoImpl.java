package com.somniuss.web.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.somniuss.web.bean.News;
import com.somniuss.web.connectionPoolProvider.ConnectionPoolProvider;
import com.somniuss.web.dao.DaoException;
import com.somniuss.web.dao.NewsDao;

public class NewsDaoImpl implements NewsDao {

    @Override
    public List<News> getAllNews() throws DaoException {
        List<News> newsList = new ArrayList<>();
        
        try (Connection connection = ConnectionPoolProvider.getConnectionPool().takeConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM news;");
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                News news = new News();
                news.setTitle(resultSet.getString("title"));
                news.setContent(resultSet.getString("content"));
                newsList.add(news);
            }
        } catch (Exception e) {
            throw new DaoException("Ошибка при получении новостей", e);
        }
        
        return newsList;
    }
}
