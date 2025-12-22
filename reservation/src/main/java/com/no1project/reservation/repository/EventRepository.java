package com.no1project.reservation.repository;

import com.no1project.reservation.model.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.sql.Statement;

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
                ORDER BY e.event_id DESC
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

    /** イベント1件取得（予約時の締切チェックなどで使用） */
    public Event findById(int eventId) {
        String sql = """
                    SELECT e.event_id, e.date, e.deadline, e.place, e.item,
                           e.company_id, e.note, c.name
                    FROM Event e
                    JOIN Company c ON e.company_id = c.company_id
                    WHERE e.event_id = ?
                """;

        List<Event> list = jdbcTemplate.query(sql, new EventRowMapper(), eventId);

        return list.isEmpty() ? null : list.get(0);
    }
    //説明会追加
    public int insert(Event e) {
        String sql = """
                    INSERT INTO Event (date, deadline, place, item, company_id, note)
                    VALUES (?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, e.getDate());
            ps.setString(2, e.getDeadline());
            ps.setString(3, e.getPlace());
            ps.setString(4, e.getItem());
            ps.setInt(5, e.getCompanyId());
            ps.setString(6, e.getNote());
            return ps;
        }, keyHolder);

        Number key = Objects.requireNonNull(keyHolder.getKey(), "generated key is null");
        return key.intValue();
    }
    //説明会情報更新
    public int update(Event e) {
        String sql = """
                    UPDATE Event
                    SET date = ?, deadline = ?, place = ?, item = ?, company_id = ?, note = ?
                    WHERE event_id = ?
                """;

        return jdbcTemplate.update(sql,
                e.getDate(),
                e.getDeadline(),
                e.getPlace(),
                e.getItem(),
                e.getCompanyId(),
                e.getNote(),
                e.getEventId());
    }
    //説明会削除
    public int deleteById(int eventId) {
        String sql = "DELETE FROM Event WHERE event_id = ?";
        return jdbcTemplate.update(sql, eventId);
    }

    //予約件数チェック
    public int countReservationsByEventId(int eventId) {
    String sql = "SELECT COUNT(*) FROM Reservation WHERE event_id = ?";
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, eventId);
    return count == null ? 0 : count;
}
}
