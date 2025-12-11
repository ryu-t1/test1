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
            r.setDeadline(rs.getString("deadline"));
            r.setPlace(rs.getString("place"));
            r.setItem(rs.getString("item"));
            r.setCompanyId(rs.getInt("company_id"));
            r.setNote(rs.getString("note"));
            r.setCompanyName(rs.getString("name"));
            return r;
        }
    }

     /** page は 0 始まり、size は 1ページの件数（今回は10） */
    public List<Event> findPage(int page, int size) {
        int offset = page * size;

        String sql = """
        SELECT e.event_id, e.date, e.deadline, e.place, e.item, e.company_id, e.note, c.name
        FROM Event e
        JOIN Company c ON e.company_id = c.company_id
        ORDER BY e.date ASC
        LIMIT ? OFFSET ?
        """;

        return jdbcTemplate.query(sql, new EventRowMapper(), size, offset);
    }

    /** 総件数 */
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM Event";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }
}
