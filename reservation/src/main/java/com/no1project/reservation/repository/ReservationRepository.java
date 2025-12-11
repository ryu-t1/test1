package com.no1project.reservation.repository;

import com.no1project.reservation.model.Reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class ReservationRowMapper implements RowMapper<Reservation> {
        @Override
        public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
            Reservation r = new Reservation();
            r.setReservationId(rs.getInt("reservation_id"));
            r.setReservationDate(rs.getString("reservation_date"));
            r.setUserId(rs.getInt("user_id"));
            r.setEventId(rs.getInt("event_id"));
            return r;
        }
    }

    /** すでに予約済みかチェック */
    public boolean existsByUserIdAndEventId(int userId, int eventId) {
        String sql = "SELECT COUNT(*) FROM Reservation WHERE user_id = ? AND event_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, eventId);
        return count != null && count > 0;
    }

    /** 予約レコードを追加 */
    public void insert(int userId, int eventId) {
        String sql = "INSERT INTO Reservation (reservation_date, user_id, event_id) " +
                     "VALUES (NOW(), ?, ?)";
        jdbcTemplate.update(sql, userId, eventId);
    }

}
