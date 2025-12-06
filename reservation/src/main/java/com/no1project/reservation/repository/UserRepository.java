package com.no1project.reservation.repository;

import com.no1project.reservation.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User u = new User();
            u.setUserId(rs.getInt("user_id"));
            u.setName(rs.getString("name"));
            u.setRole(rs.getString("role"));
            u.setEmail(rs.getString("email"));
            u.setPassword(rs.getString("password"));
            return u;
        }
    }

    // email 重複チェック用
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM Users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    // ログインなど用
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE email = ?";
        return jdbcTemplate.query(sql, new UserRowMapper(), email)
                           .stream()
                           .findFirst();
    }

    // ★ 新規登録用：INSERT して採番された user_id を User にセットして返す
    public User insert(User user) {
        String sql = "INSERT INTO Users (name, role, email, password) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getName());
            ps.setString(2, user.getRole());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            user.setUserId(key.intValue());
        }

        return user;
    }
}
