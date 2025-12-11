package com.no1project.reservation.service;

import com.no1project.reservation.model.Event;
import com.no1project.reservation.repository.EventRepository;
import com.no1project.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final EventRepository eventRepository;

    // DB の形式: "yyyy-MM-dd HH:mm:ss"
    private static final DateTimeFormatter DEADLINE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ReservationService(ReservationRepository reservationRepository,
                              EventRepository eventRepository) {
        this.reservationRepository = reservationRepository;
        this.eventRepository = eventRepository;
    }

    public void reserve(int userId, int eventId) {
        // イベントを取得
        Event event = eventRepository.findById(eventId);
        if (event == null) {
            throw new IllegalArgumentException("イベントが存在しません");
        }

        // 締切チェック（時刻まで正確に）
        String deadlineStr = event.getDeadline(); // "2026-04-01 09:59:00"
        if (deadlineStr != null && !deadlineStr.isBlank()) {
            try {
                LocalDateTime deadline = LocalDateTime.parse(deadlineStr, DEADLINE_FORMATTER);

                if (deadline.isBefore(LocalDateTime.now())) {
                    throw new IllegalStateException("締切を過ぎているため予約できません");
                }

            } catch (DateTimeParseException e) {
                throw new IllegalStateException("締切日時の形式が不正です: " + deadlineStr);
            }
        }

        // 二重予約チェック
        if (reservationRepository.existsByUserIdAndEventId(userId, eventId)) {
            throw new IllegalStateException("すでにこの説明会を予約しています");
        }

        // 予約を登録
        reservationRepository.insert(userId, eventId);
    }
}
