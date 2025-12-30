package com.no1project.reservation.repository;

import com.no1project.reservation.model.Reservation;
import com.no1project.reservation.dto.ReservationViewDto;
import com.no1project.reservation.dto.ReservationAttendeeDto;
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

    private static class ReservationViewRowMapper implements RowMapper<ReservationViewDto> {
        @Override
        public ReservationViewDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            ReservationViewDto dto = new ReservationViewDto();
            dto.setReservationId(rs.getInt("reservation_id"));
            dto.setReservationDate(rs.getString("reservation_date"));

            dto.setEventId(rs.getInt("event_id"));
            dto.setEventDate(rs.getString("date"));
            dto.setDeadline(rs.getString("deadline"));
            dto.setPlace(rs.getString("place"));
            dto.setItem(rs.getString("item"));
            dto.setNote(rs.getString("note"));
            dto.setCompanyName(rs.getString("company_name"));
            return dto;
        }
    }

    private static class ReservationAttendeeRowMapper implements RowMapper<ReservationAttendeeDto> {
        @Override
        public ReservationAttendeeDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            ReservationAttendeeDto dto = new ReservationAttendeeDto();
            dto.setUserId(rs.getInt("user_id"));
            dto.setName(rs.getString("name"));
            dto.setGrade(rs.getInt("grade"));
            dto.setMyClass(rs.getString("class"));
            dto.setNumber(rs.getInt("number"));
            dto.setReservationDate(rs.getString("reservation_date"));
            return dto;
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

    /** 自分の予約一覧（イベント＋企業名も含める） */
    public List<ReservationViewDto> findMyReservations(int userId) {
        String sql = """
                    SELECT
                      r.reservation_id,
                      r.reservation_date,
                      e.event_id,
                      e.date,
                      e.deadline,
                      e.place,
                      e.item,
                      e.note,
                      c.name AS company_name
                    FROM Reservation r
                    JOIN Event e ON r.event_id = e.event_id
                    JOIN Company c ON e.company_id = c.company_id
                    WHERE r.user_id = ?
                    ORDER BY e.date ASC
                """;

        return jdbcTemplate.query(sql, new ReservationViewRowMapper(), userId);
    }

    // 予約の締め切りを取得
    public String findDeadlineByReservationIdAndUserId(int reservationId, int userId) {
        String sql = """
                    SELECT e.deadline
                    FROM Reservation r
                    JOIN Event e ON r.event_id = e.event_id
                    WHERE r.reservation_id = ?
                      AND r.user_id = ?
                """;
        List<String> list = jdbcTemplate.query(sql, (rs, i) -> rs.getString("deadline"), reservationId, userId);
        return list.isEmpty() ? null : list.get(0);
    }

    // 自分の予約を削除
    public int deleteByReservationIdAndUserId(int reservationId, int userId) {
        String sql = "DELETE FROM Reservation WHERE reservation_id = ? AND user_id = ?";
        return jdbcTemplate.update(sql, reservationId, userId);
    }

    public List<ReservationAttendeeDto> findAttendeesByEventId(int eventId) {
        String sql = """
                    SELECT
                      r.user_id,
                      u.name,
                      s.grade,
                      s.`class`,
                      s.number,
                      r.reservation_date
                    FROM Reservation r
                    JOIN Users u ON r.user_id = u.user_id
                    JOIN Student s ON r.user_id = s.user_id
                    WHERE r.event_id = ?
                    ORDER BY s.grade ASC, s.`class` ASC, s.number ASC
                """;

        return jdbcTemplate.query(sql, new ReservationAttendeeRowMapper(), eventId);
    }
}
