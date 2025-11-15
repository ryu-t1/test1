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

    // 予約一覧
    public List<Reservation> findAll() {
        String sql = "SELECT * FROM reservations ORDER BY reservation_id";
        return jdbcTemplate.query(sql, new ReservationRowMapper());
    }

    // 予約登録
    public int insert(Reservation reservation) {
        String sql = """
            INSERT INTO reservations (user_id, reserved_date, reserved_time, memo)
            VALUES (?, ?, ?, ?)
            """;
        return jdbcTemplate.update(
                sql,
                reservation.getUserId(),
                reservation.getReservedDate(),
                reservation.getReservedTime(),
                reservation.getMemo()
        );
    }

    // ★ 変更ポイント１：
    // reservation_id と user_id の両方が一致したものだけ削除する
    public int deleteByIdAndUserId(int reservationId, int userId) {
        String sql = "DELETE FROM reservations WHERE reservation_id = ? AND user_id = ?";
        return jdbcTemplate.update(sql, reservationId, userId);
    }

    // userId で絞り込み
    public List<Reservation> findByUserId(int userId) {
        String sql = "SELECT * FROM reservations WHERE user_id = ? ORDER BY reserved_date, reserved_time";
        return jdbcTemplate.query(sql, new ReservationRowMapper(), userId);
    }

    private static class ReservationRowMapper implements RowMapper<Reservation> {
        @Override
        public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
            Reservation r = new Reservation();
            r.setReservationId(rs.getInt("reservation_id"));
            r.setUserId(rs.getInt("user_id"));
            r.setReservedDate(rs.getString("reserved_date")); // DATE → String
            r.setReservedTime(rs.getString("reserved_time")); // TIME → String
            r.setMemo(rs.getString("memo"));
            return r;
        }
    }
}
