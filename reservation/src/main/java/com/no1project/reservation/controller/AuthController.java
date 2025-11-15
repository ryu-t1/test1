package com.no1project.reservation.controller;

import com.no1project.reservation.security.CustomUserDetails;
import com.no1project.reservation.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // リクエストDTO
    public static class LoginRequest {
        public String email;
        public String password;
    }

    // レスポンスDTO
    public static class LoginResponse {
        public String token;
        public int userId;
        public String email;
        public String name;

        public LoginResponse(String token, int userId, String email, String name) {
            this.token = token;
            this.userId = userId;
            this.email = email;
            this.name = name;
        }
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        // Controller ログイン方式：ここから AuthenticationManager を呼び出す
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(request.email, request.password);

        Authentication authResult = authenticationManager.authenticate(authRequest);

        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        String token = jwtUtil.generateToken(userDetails.getUsername());

        return new LoginResponse(
                token,
                userDetails.getUserId(),
                userDetails.getEmail(),
                userDetails.getName()
        );
    }
}
