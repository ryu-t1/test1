package com.no1project.reservation.repository;//テスト用です。

import com.no1project.reservation.model.SampleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SampleUserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SampleUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 全ユーザー取得
    public List<SampleUser> findAll() {
        String sql = "SELECT * FROM users_backup";
        return jdbcTemplate.query(sql, new SampleUserRowMapper());
    }

    // IDで取得
    public SampleUser findById(int id) {
        String sql = "SELECT * FROM users_backup WHERE user_id = ?";
        List<SampleUser> users = jdbcTemplate.query(sql, new SampleUserRowMapper(), id);
        return users.isEmpty() ? null : users.get(0);
    }

    // ★ 追加：emailで取得（ログイン用）
    public SampleUser findByEmail(String email) {
        String sql = "SELECT * FROM users_backup WHERE email = ?";
        List<SampleUser> users = jdbcTemplate.query(sql, new SampleUserRowMapper(), email);
        return users.isEmpty() ? null : users.get(0);
    }

    // RowMapper
    private static class SampleUserRowMapper implements RowMapper<SampleUser> {
        @Override
        public SampleUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            SampleUser u = new SampleUser();
            u.setUserId(rs.getInt("user_id"));
            u.setPassword(rs.getString("password"));
            u.setEmail(rs.getString("email"));
            u.setName(rs.getString("name"));
            return u;
        }
    }
}
