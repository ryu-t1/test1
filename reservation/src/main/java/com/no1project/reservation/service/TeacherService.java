package com.no1project.reservation.service;

import com.no1project.reservation.model.Teacher;
import com.no1project.reservation.dto.TeacherRegisterRequest;
import com.no1project.reservation.model.User;
import com.no1project.reservation.repository.TeacherRepository;
import com.no1project.reservation.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeacherService {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    public TeacherService(UserRepository userRepository,
                          TeacherRepository teacherRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerTeacher(TeacherRegisterRequest req) {

        // email 重複チェック
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("このメールアドレスはすでに登録されています。");
        }

        // 1. User 作成
        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setRole("normal_admin"); // ★ ここが student と違う
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        userRepository.insert(user); // userId がセットされる想定

        // 2. Teacher 作成
        Teacher teacher = new Teacher();
        teacher.setUserId(user.getUserId());
        teacher.setGrade(req.getGrade());
        teacher.setMyClass(req.getMyClass());

        teacherRepository.insert(teacher);
    }
}
