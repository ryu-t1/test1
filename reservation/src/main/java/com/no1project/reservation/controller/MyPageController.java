package com.no1project.reservation.controller;

import com.no1project.reservation.dto.MeResponse;
import com.no1project.reservation.dto.UpdateMyEmailRequest;
import com.no1project.reservation.dto.UpdateMyPasswordRequest;
import com.no1project.reservation.security.CustomUserDetails;
import com.no1project.reservation.service.MyPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
public class MyPageController {

    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    // ログインユーザーの userId を取り出す共通関数
    private int getLoginUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            throw new IllegalStateException("認証情報が不正です。");
        }
        return ((CustomUserDetails) principal).getUserId();
    }

    @GetMapping
    public ResponseEntity<MeResponse> getMe(Authentication authentication) {
        int userId = getLoginUserId(authentication);
        MeResponse res = myPageService.getMe(userId);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/email")
    public ResponseEntity<?> updateEmail(@RequestBody UpdateMyEmailRequest req,
                                         Authentication authentication) {
        int userId = getLoginUserId(authentication);
        myPageService.updateMyEmail(userId, req.getNewEmail());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdateMyPasswordRequest req,
                                           Authentication authentication) {
        int userId = getLoginUserId(authentication);
        myPageService.updateMyPassword(userId, req.getCurrentPassword(), req.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
