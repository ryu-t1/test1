package com.no1project.reservation.repository;

import com.no1project.reservation.model.Teacher;
import com.no1project.reservation.dto.TeacherRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TeacherRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TeacherRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class TeacherRowMapper implements RowMapper<Teacher> {
        @Override
        public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
            Teacher r = new Teacher();
            r.setUserId(rs.getInt("user_id"));
            r.setGrade(rs.getInt("grade"));
            r.setMyClass(rs.getString("class"));
            return r;
        }
    }

    public int insert(Teacher teacher) {
        String sql = "INSERT INTO Teacher (user_id, grade, class) VALUES (?, ?, ?)";

        return jdbcTemplate.update(
                sql,
                teacher.getUserId(),
                teacher.getGrade(),
                teacher.getMyClass()
        );
    }
}
