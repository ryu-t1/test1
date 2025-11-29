package com.no1project.reservation.repository;

import com.no1project.reservation.model.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EventRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class EventRowMapper implements RowMapper<Event> {
        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            Event r = new Event();
            r.setEventId(rs.getInt("event_id"));
            r.setDate(rs.getString("date"));
            r.setPlace(rs.getString("place"));
            r.setItem(rs.getString("item"));
            r.setCompanyId(rs.getInt("company_id"));
            r.setNote(rs.getString("note"));
            return r;
        }
    }

}
