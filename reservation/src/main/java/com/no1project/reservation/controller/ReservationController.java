package com.no1project.reservation.controller;

import com.no1project.reservation.dto.ReservationAttendeeDto;
import com.no1project.reservation.security.CustomUserDetails;
import com.no1project.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events/{eventId}/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<?> reserve(@PathVariable int eventId,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // ここで role はサーバ側の Spring Security でチェックするので、
        // コントローラ内では userId だけ使えばOK。
        int userId = userDetails.getUserId();
        reservationService.reserve(userId, eventId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/attendees")
    public ResponseEntity<List<ReservationAttendeeDto>> attendees(@PathVariable int eventId) {
        return ResponseEntity.ok(reservationService.getAttendees(eventId));
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> count(@PathVariable int eventId) {
        return ResponseEntity.ok(reservationService.getReservationCount(eventId));
    }
}
