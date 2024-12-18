package com.somniuss.web.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import com.somniuss.web.bean.User;
import com.somniuss.web.connectionPoolProvider.ConnectionPoolProvider;
import com.somniuss.web.dao.DaoException;
import com.somniuss.web.dao.UserDao;
import com.somniuss.web.сonnectionPool.ConnectionPoolException;

public class SQLUserDao implements UserDao {

    @Override
    public User registration(String name, String email, String password) throws DaoException {
        String hashedPassword = hashPassword(password);

        String query = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        try (Connection con = ConnectionPoolProvider.getConnectionPool().takeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, hashedPassword);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return new User(name, email, hashedPassword);
            } else {
                throw new DaoException("Ошибка при добавлении пользователя");
            }
        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException("Ошибка при регистрации пользователя", e);
        }
    }

    @Override
    public boolean isUserExistsByName(String name) throws DaoException {
        String query = "SELECT 1 FROM users WHERE name = ?";
        try (Connection con = ConnectionPoolProvider.getConnectionPool().takeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException("Ошибка проверки существования пользователя по имени", e);
        }
    }

    @Override
    public boolean isUserExistsByEmail(String email) throws DaoException {
        String query = "SELECT 1 FROM users WHERE email = ?";
        try (Connection con = ConnectionPoolProvider.getConnectionPool().takeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException("Ошибка проверки существования пользователя по email", e);
        }
    }

    @Override
    public User authorization(String name, String password) throws DaoException {
        String query = "SELECT * FROM users WHERE name = ?";

        try (Connection con = ConnectionPoolProvider.getConnectionPool().takeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    if (checkPassword(password, hashedPassword)) {
                        return new User(
                            rs.getString("name"),
                            rs.getString("email"),
                            hashedPassword
                        );
                    }
                }
            }
        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException("Ошибка при авторизации пользователя", e);
        }

        return null;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
