package com.no1project.reservation.controller;

import com.no1project.reservation.dto.TeacherRegisterRequest;
import com.no1project.reservation.service.TeacherService;
import com.no1project.reservation.controller.AuthController.LoginRequest;
import com.no1project.reservation.controller.AuthController.LoginResponse;
import com.no1project.reservation.dto.StudentRegisterRequest;
import com.no1project.reservation.security.CustomUserDetails;
import com.no1project.reservation.security.JwtUtil;
import com.no1project.reservation.service.StudentService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
     private final StudentService studentService;
     private final TeacherService teacherService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          StudentService studentService,
                        TeacherService teacherService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.studentService = studentService;
         this.teacherService = teacherService;
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
        public String role;

        public LoginResponse(String token, int userId, String email, String name, String role) {
            this.token = token;
            this.userId = userId;
            this.email = email;
            this.name = name;
            this.role = role;
        }
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        // Controller ログイン方式：ここから AuthenticationManager を呼び出す
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(request.email, request.password);

        Authentication authResult = authenticationManager.authenticate(authRequest);

        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        return new LoginResponse(
                token,
                userDetails.getUserId(),
                userDetails.getEmail(),
                userDetails.getName(),
                userDetails.getRole()
        );
        
    }
    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@RequestBody StudentRegisterRequest request) {
        studentService.registerStudent(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<?> registerTeacher(@RequestBody TeacherRegisterRequest request) {
        teacherService.registerTeacher(request);
        return ResponseEntity.ok().build();
    }
}
