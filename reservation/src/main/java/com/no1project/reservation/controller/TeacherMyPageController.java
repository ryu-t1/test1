package com.no1project.reservation.controller;

import com.no1project.reservation.dto.TeacherMeResponse;
import com.no1project.reservation.dto.UpdateMyEmailRequest;
import com.no1project.reservation.dto.UpdateMyPasswordRequest;
import com.no1project.reservation.security.CustomUserDetails;
import com.no1project.reservation.service.TeacherMyPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher/me")
public class TeacherMyPageController {

    private final TeacherMyPageService teacherMyPageService;

    public TeacherMyPageController(TeacherMyPageService teacherMyPageService) {
        this.teacherMyPageService = teacherMyPageService;
    }

    private int getLoginUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            throw new IllegalStateException("認証情報が不正です。");
        }
        return ((CustomUserDetails) principal).getUserId();
    }

    @GetMapping
    public ResponseEntity<TeacherMeResponse> getMe(Authentication authentication) {
        int userId = getLoginUserId(authentication);
        return ResponseEntity.ok(teacherMyPageService.getMe(userId));
    }

    @PutMapping("/email")
    public ResponseEntity<?> updateEmail(@RequestBody UpdateMyEmailRequest req,
                                         Authentication authentication) {
        int userId = getLoginUserId(authentication);
        teacherMyPageService.updateMyEmail(userId, req.getNewEmail());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdateMyPasswordRequest req,
                                            Authentication authentication) {
        int userId = getLoginUserId(authentication);
        teacherMyPageService.updateMyPassword(userId, req.getCurrentPassword(), req.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
