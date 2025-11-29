package com.no1project.reservation.controller;// テスト用です。

import com.no1project.reservation.model.SampleReservation;
import com.no1project.reservation.security.CustomUserDetails;
import com.no1project.reservation.service.SampleReservationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class SampleReservationController {

    private final SampleReservationService reservationService;

    public SampleReservationController(SampleReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // 予約一覧（全件）※今は全件、あとで「自分の予約だけ」に変えてもOK
    @GetMapping
    public List<SampleReservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    // 必要なら userId で絞り込み（残しておいてもよい）
    @GetMapping("/user/{userId}")
    public List<SampleReservation> getReservationsByUser(@PathVariable int userId) {
        return reservationService.getReservationsByUserId(userId);
    }

    // リクエスト用DTO（userIdは含めない）
    public static class CreateReservationRequest {
        public String reservedDate;
        public String reservedTime;
        public String memo;
    }

    // 予約登録：userIdはJWTから取得する
    @PostMapping
    public void createReservation(
            @RequestBody CreateReservationRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        SampleReservation reservation = new SampleReservation();
        reservation.setUserId(userDetails.getUserId()); // ★ ログインユーザー
        reservation.setReservedDate(request.reservedDate);
        reservation.setReservedTime(request.reservedTime);
        reservation.setMemo(request.memo);

        reservationService.addReservation(reservation);
    }

    // ★ 変更ポイント３：
    // 削除も JWT からログインユーザーを取り出して、
    // 「自分の予約だけ」削除できるようにする
    @DeleteMapping("/{id}")
    public void deleteReservation(
            @PathVariable("id") int reservationId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean deleted = reservationService.deleteReservationForUser(
                reservationId,
                userDetails.getUserId()
        );

        // deleted == false の場合は「他人の予約 or 存在しない」なので、
        // ここで何もしなければ実質「削除されない」状態になる。
        // （本気でやるなら 404 / 403 を返す実装にしてもOK）
    }
}
