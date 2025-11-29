package com.no1project.reservation.repository;//テスト用です。

import com.no1project.reservation.model.SampleReservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SampleReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SampleReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 予約一覧
    public List<SampleReservation> findAll() {
        String sql = "SELECT * FROM reservations_backup ORDER BY reservation_id";
        return jdbcTemplate.query(sql, new SampleReservationRowMapper());
    }

    // 予約登録
    public int insert(SampleReservation reservation) {
        String sql = """
            INSERT INTO reservations_backup (user_id, reserved_date, reserved_time, memo)
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
        String sql = "DELETE FROM reservations_backup WHERE reservation_id = ? AND user_id = ?";
        return jdbcTemplate.update(sql, reservationId, userId);
    }

    // userId で絞り込み
    public List<SampleReservation> findByUserId(int userId) {
        String sql = "SELECT * FROM reservations_backup WHERE user_id = ? ORDER BY reserved_date, reserved_time";
        return jdbcTemplate.query(sql, new SampleReservationRowMapper(), userId);
    }

    private static class SampleReservationRowMapper implements RowMapper<SampleReservation> {
        @Override
        public SampleReservation mapRow(ResultSet rs, int rowNum) throws SQLException {
            SampleReservation r = new SampleReservation();
            r.setReservationId(rs.getInt("reservation_id"));
            r.setUserId(rs.getInt("user_id"));
            r.setReservedDate(rs.getString("reserved_date")); // DATE → String
            r.setReservedTime(rs.getString("reserved_time")); // TIME → String
            r.setMemo(rs.getString("memo"));
            return r;
        }
    }
}
