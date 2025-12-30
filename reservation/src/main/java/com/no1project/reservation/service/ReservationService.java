package com.no1project.reservation.service;

import com.no1project.reservation.model.Event;
import com.no1project.reservation.dto.ReservationViewDto;
import com.no1project.reservation.dto.ReservationAttendeeDto;
import com.no1project.reservation.repository.EventRepository;
import com.no1project.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final EventRepository eventRepository;

    // DB の形式: "yyyy-MM-dd HH:mm:ss"
    private static final DateTimeFormatter DEADLINE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter EVENT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    public MyReservationsResponse getMyReservations(int userId) {
        List<ReservationViewDto> all = reservationRepository.findMyReservations(userId);

        List<ReservationViewDto> upcoming = new ArrayList<>();
        List<ReservationViewDto> past = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        for (ReservationViewDto r : all) {
            String eventDateStr = r.getEventDate(); // "yyyy-MM-dd HH:mm:ss" 想定
            try {
                LocalDateTime eventDate = LocalDateTime.parse(eventDateStr, EVENT_DATE_FORMATTER);
                if (eventDate.isBefore(now)) {
                    past.add(r);
                } else {
                    upcoming.add(r);
                }
            } catch (DateTimeParseException e) {
                // 形式不正なら、とりあえず upcoming 側に入れて「表示はする」方が安全
                upcoming.add(r);
            }
        }

        return new MyReservationsResponse(upcoming, past);
    }

    /** フロント返却用DTO */
    public static class MyReservationsResponse {
        private List<ReservationViewDto> upcoming;
        private List<ReservationViewDto> past;

        public MyReservationsResponse(List<ReservationViewDto> upcoming, List<ReservationViewDto> past) {
            this.upcoming = upcoming;
            this.past = past;
        }

        public List<ReservationViewDto> getUpcoming() {
            return upcoming;
        }

        public List<ReservationViewDto> getPast() {
            return past;
        }
    }

    // 締切日を過ぎていなければ自分の予約を削除
    public void cancelMyReservation(int userId, int reservationId) {

        String deadlineStr = reservationRepository.findDeadlineByReservationIdAndUserId(reservationId, userId);

        if (deadlineStr == null) {
            // 存在しない or 他人の予約
            throw new IllegalStateException("削除できません（予約が存在しない、または権限がありません）");
        }

        // deadline が空/NULLならキャンセルOK扱いにするならこのまま
        if (deadlineStr != null && !deadlineStr.isBlank()) {
            try {
                LocalDateTime deadline = LocalDateTime.parse(deadlineStr, DEADLINE_FORMATTER);
                if (deadline.isBefore(LocalDateTime.now())) {
                    throw new IllegalStateException("締切を過ぎているためキャンセルできません");
                }
            } catch (DateTimeParseException e) {
                throw new IllegalStateException("締切日時の形式が不正です: " + deadlineStr);
            }
        }

        int deleted = reservationRepository.deleteByReservationIdAndUserId(reservationId, userId);
        if (deleted == 0) {
            throw new IllegalStateException("削除できません（予約が存在しない、または権限がありません）");
        }
    }

    public List<ReservationAttendeeDto> getAttendees(int eventId) {
        // event存在チェック（任意だけど入れておくと親切）
        Event event = eventRepository.findById(eventId);
        if (event == null) {
            throw new IllegalArgumentException("イベントが存在しません");
        }
        return reservationRepository.findAttendeesByEventId(eventId);
    }

}
