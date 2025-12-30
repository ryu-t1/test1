package com.no1project.reservation.repository;

import com.no1project.reservation.model.Company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.List;
import java.util.Objects;

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

    // ★新規登録（AUTO_INCREMENT の company_id を返す）
    public int insert(Company company) {
        String sql = "INSERT INTO Company (name, address, website) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, company.getName());
            ps.setString(2, company.getAddress());
            ps.setString(3, company.getWebsite());
            return ps;
        }, keyHolder);

        Number key = Objects.requireNonNull(keyHolder.getKey(), "generated key is null");
        return key.intValue();
    }

    // 10件ずつ取得（offset は (page-1)*size）
    public List<Company> findPage(int limit, int offset) {
        String sql = """
                    SELECT company_id, name, address, website
                    FROM Company
                    ORDER BY company_id DESC
                    LIMIT ? OFFSET ?
                """;
        return jdbcTemplate.query(sql, new CompanyRowMapper(), limit, offset);
    }

    // 全件数
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM Company";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count == null ? 0 : count;
    }

    // ★1件取得（編集後に返すなどで利用）
    public Company findById(int companyId) {
        String sql = """
                    SELECT company_id, name, address, website
                    FROM Company
                    WHERE company_id = ?
                """;
        List<Company> list = jdbcTemplate.query(sql, new CompanyRowMapper(), companyId);
        return list.isEmpty() ? null : list.get(0);
    }

    // ★更新
    public int update(Company c) {
        String sql = """
                    UPDATE Company
                    SET name = ?, address = ?, website = ?
                    WHERE company_id = ?
                """;
        return jdbcTemplate.update(sql,
                c.getName(),
                c.getAddress(),
                c.getWebsite(),
                c.getCompanyId());
    }

    // ★削除（紐づきチェックは Service でやる）
    public int deleteById(int companyId) {
        String sql = "DELETE FROM Company WHERE company_id = ?";
        return jdbcTemplate.update(sql, companyId);
    }

    // ★その会社に紐づく説明会件数（Eventテーブル）
    public int countEventsByCompanyId(int companyId) {
        String sql = "SELECT COUNT(*) FROM Event WHERE company_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, companyId);
        return count == null ? 0 : count;
    }

    // CompanyRepository.java に追記

    public List<Company> findPageBySearch(String qName, String qAddress, int limit, int offset) {
        String sql = """
                    SELECT company_id, name, address, website
                    FROM Company
                    WHERE (? IS NULL OR ? = '' OR name LIKE CONCAT('%', ?, '%'))
                      AND (? IS NULL OR ? = '' OR address LIKE CONCAT('%', ?, '%'))
                    ORDER BY company_id DESC
                    LIMIT ? OFFSET ?
                """;

        return jdbcTemplate.query(
                sql,
                new CompanyRowMapper(),
                qName, qName, qName,
                qAddress, qAddress, qAddress,
                limit, offset);
    }

    public int countBySearch(String qName, String qAddress) {
        String sql = """
                    SELECT COUNT(*)
                    FROM Company
                    WHERE (? IS NULL OR ? = '' OR name LIKE CONCAT('%', ?, '%'))
                      AND (? IS NULL OR ? = '' OR address LIKE CONCAT('%', ?, '%'))
                """;

        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                qName, qName, qName,
                qAddress, qAddress, qAddress);
        return count == null ? 0 : count;
    }

}
