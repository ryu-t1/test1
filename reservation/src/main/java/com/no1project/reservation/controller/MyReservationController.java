package com.no1project.reservation.controller;

import com.no1project.reservation.security.CustomUserDetails;
import com.no1project.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class MyReservationController {

    private final ReservationService reservationService;

    public MyReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/me")
    public ReservationService.MyReservationsResponse myReservations(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        int userId = userDetails.getUserId();
        return reservationService.getMyReservations(userId);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> cancel(@PathVariable int reservationId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        int userId = userDetails.getUserId();

        try {
            reservationService.cancelMyReservation(userId, reservationId);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

}
