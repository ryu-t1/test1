package com.no1project.reservation.service;

import com.no1project.reservation.model.SampleReservation;
import com.no1project.reservation.repository.SampleReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SampleReservationService {

    private final SampleReservationRepository reservationRepository;

    public SampleReservationService(SampleReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<SampleReservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<SampleReservation> getReservationsByUserId(int userId) {
        return reservationRepository.findByUserId(userId);
    }

    public void addReservation(SampleReservation reservation) {
        reservationRepository.insert(reservation);
    }

    // ★ 変更ポイント２：
    // 「誰の予約か(userId)」を受け取って削除する
    public boolean deleteReservationForUser(int reservationId, int userId) {
        int rows = reservationRepository.deleteByIdAndUserId(reservationId, userId);
        return rows > 0; // true: 削除された / false: そもそも自分の予約じゃない or 存在しない
    }
}
