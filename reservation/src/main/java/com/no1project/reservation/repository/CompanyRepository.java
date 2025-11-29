package com.no1project.reservation.repository;

import com.no1project.reservation.model.Company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CompanyRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CompanyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class CompanyRowMapper implements RowMapper<Company> {
        @Override
        public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
            Company r = new Company();
            r.setCompanyId(rs.getInt("company_id"));
            r.setName(rs.getString("name"));
            r.setAddress(rs.getString("address"));
            r.setWebsite(rs.getString("website"));
            return r;
        }
    }
}
