package com.no1project.reservation.service;

import com.no1project.reservation.dto.*;
import com.no1project.reservation.model.Student;
import com.no1project.reservation.model.Teacher;
import com.no1project.reservation.model.User;
import com.no1project.reservation.repository.StudentRepository;
import com.no1project.reservation.repository.TeacherRepository;
import com.no1project.reservation.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuperAdminUserManageService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public SuperAdminUserManageService(
            UserRepository userRepository,
            StudentRepository studentRepository,
            TeacherRepository teacherRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    public static class PageResponse<T> {
        private List<T> items;
        private int page;
        private int size;
        private int total;

        public PageResponse(List<T> items, int page, int size, int total) {
            this.items = items;
            this.page = page;
            this.size = size;
            this.total = total;
        }

        public List<T> getItems() {
            return items;
        }

        public int getPage() {
            return page;
        }

        public int getSize() {
            return size;
        }

        public int getTotal() {
            return total;
        }
    }

    public PageResponse<AdminUserRowDto> listUsers(String q, String role, int page, int size) {
        if (page < 0)
            page = 0;
        if (size <= 0)
            size = 10;
        if (size > 50)
            size = 50;

        // roleは完全一致想定（student / normal_admin / super_admin）
        String roleFilter = (role == null || role.isBlank()) ? null : role.trim();

        int offset = page * size;
        List<User> users = userRepository.findPaged(q, roleFilter, size, offset);
        int total = userRepository.countAll(q, roleFilter);

        List<AdminUserRowDto> rows = new ArrayList<>();
        for (User u : users) {
            AdminUserRowDto dto = new AdminUserRowDto();
            dto.setUserId(u.getUserId());
            dto.setName(u.getName());
            dto.setEmail(u.getEmail());
            dto.setRole(u.getRole());

            String r = (u.getRole() == null) ? "" : u.getRole().toLowerCase();
            if (r.equals("student")) {
                studentRepository.findByUserId(u.getUserId()).ifPresent(s -> {
                    dto.setGrade(s.getGrade());
                    dto.setMyClass(s.getMyClass());
                    dto.setNumber(s.getNumber());
                });
            } else if (r.equals("normal_admin")) {
                teacherRepository.findByUserId(u.getUserId()).ifPresent(t -> {
                    dto.setGrade(t.getGrade());
                    dto.setMyClass(t.getMyClass());
                });
            }
            rows.add(dto);
        }

        return new PageResponse<>(rows, page, size, total);
    }

    @Transactional
    public void updateStudentProfile(int userId, UpdateStudentProfileRequest req) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザが存在しません"));

        if (!"student".equalsIgnoreCase(u.getRole())) {
            throw new IllegalArgumentException("studentユーザではありません");
        }

        int updated = studentRepository.updateProfile(userId, req.getGrade(), req.getMyClass(), req.getNumber());
        if (updated == 0)
            throw new IllegalArgumentException("student情報が存在しません");
    }

    @Transactional
    public void updateTeacherProfile(int userId, UpdateTeacherProfileRequest req) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザが存在しません"));

        if (!"normal_admin".equalsIgnoreCase(u.getRole())) {
            throw new IllegalArgumentException("normal_adminユーザではありません");
        }

        int updated = teacherRepository.updateProfile(userId, req.getGrade(), req.getMyClass());
        if (updated == 0)
            throw new IllegalArgumentException("teacher情報が存在しません");
    }

    @Transactional
    public int batchUpdateStudentGrade(BatchUpdateStudentGradeRequest req) {
        int delta = req.getDelta();

        // 1 か -1 以外は禁止
        if (delta != 1 && delta != -1) {
            throw new IllegalArgumentException("delta は +1 または -1 のみ指定できます");
        }

        return studentRepository.batchShiftGrade(delta);
    }
}
