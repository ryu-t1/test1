package com.no1project.reservation.service;

import com.no1project.reservation.model.Reservation;
import com.no1project.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsByUserId(int userId) {
        return reservationRepository.findByUserId(userId);
    }

    public void addReservation(Reservation reservation) {
        reservationRepository.insert(reservation);
    }

    // ★ 変更ポイント２：
    // 「誰の予約か(userId)」を受け取って削除する
    public boolean deleteReservationForUser(int reservationId, int userId) {
        int rows = reservationRepository.deleteByIdAndUserId(reservationId, userId);
        return rows > 0; // true: 削除された / false: そもそも自分の予約じゃない or 存在しない
    }
}
