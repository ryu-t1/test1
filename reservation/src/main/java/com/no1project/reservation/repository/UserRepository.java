package com.no1project.reservation.repository;
import com.no1project.reservation.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 获取所有用户
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    // 根据ID获取单个用户
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), id);
        return users.isEmpty() ? null : users.get(0);
    }

    // RowMapper
    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User u = new User();
            u.setUserId(rs.getInt("user_id"));
            u.setPassword(rs.getString("password"));
            u.setEmail(rs.getString("email"));
            u.setName(rs.getString("name"));
            return u;
        }
    }
}
