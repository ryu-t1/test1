package com.no1project.reservation.controller;

import com.no1project.reservation.dto.*;
import com.no1project.reservation.service.SuperAdminUserManageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/super-admin")
public class SuperAdminUserManageController {

    private final SuperAdminUserManageService service;

    public SuperAdminUserManageController(SuperAdminUserManageService service) {
        this.service = service;
    }

    @GetMapping("/users")
  public ResponseEntity<?> listUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String q,
        @RequestParam(required = false) String role   // ★追加
) {
    return ResponseEntity.ok(service.listUsers(q, role, page, size));
}

    @PutMapping("/users/{userId}/student")
    public ResponseEntity<?> updateStudent(
            @PathVariable int userId,
            @RequestBody UpdateStudentProfileRequest req
    ) {
        service.updateStudentProfile(userId, req);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{userId}/teacher")
    public ResponseEntity<?> updateTeacher(
            @PathVariable int userId,
            @RequestBody UpdateTeacherProfileRequest req
    ) {
        service.updateTeacherProfile(userId, req);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/students/grade-batch")
    public ResponseEntity<?> batchGrade(@RequestBody BatchUpdateStudentGradeRequest req) {
        int updated = service.batchUpdateStudentGrade(req);
        return ResponseEntity.ok(updated);
    }
}
