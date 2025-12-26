package com.no1project.reservation.controller;

import com.no1project.reservation.dto.SuperAdminMeResponse;
import com.no1project.reservation.security.CustomUserDetails;
import com.no1project.reservation.service.SuperAdminMyPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/super/me")
public class SuperAdminMyPageController {

    private final SuperAdminMyPageService service;

    public SuperAdminMyPageController(SuperAdminMyPageService service) {
        this.service = service;
    }

    private int getLoginUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            throw new IllegalStateException("認証情報が不正です。");
        }
        return ((CustomUserDetails) principal).getUserId();
    }

    @GetMapping
    public ResponseEntity<SuperAdminMeResponse> getMe(Authentication authentication) {
        int userId = getLoginUserId(authentication);
        return ResponseEntity.ok(service.getMe(userId));
    }
}
