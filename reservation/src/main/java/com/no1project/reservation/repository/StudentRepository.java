package com.no1project.reservation.repository;

import com.no1project.reservation.model.Student;
import com.no1project.reservation.dto.StudentRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class StudentRowMapper implements RowMapper<Student> {
        @Override
        public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
            Student r = new Student();
            r.setUserId(rs.getInt("user_id"));
            r.setGrade(rs.getInt("grade"));
            r.setMyClass(rs.getString("class"));
            r.setNumber(rs.getInt("number"));
            return r;
        }
    }

     public int insert(Student student) {
        String sql = "INSERT INTO Student (user_id, grade, class, number) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                student.getUserId(),
                student.getGrade(),
                student.getMyClass(),
                student.getNumber()
        );
    }
}
