package com.no1project.reservation.service;

import com.no1project.reservation.model.Student;
import com.no1project.reservation.dto.StudentRegisterRequest;
import com.no1project.reservation.model.User;
import com.no1project.reservation.repository.StudentRepository;
import com.no1project.reservation.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(UserRepository userRepository,
                                 StudentRepository studentRepository,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerStudent(StudentRegisterRequest req) {

        // email 重複チェック
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("このメールアドレスはすでに登録されています。");
        }

        // 1. User を作成
        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setRole("student");  // 学生固定
        user.setPassword(passwordEncoder.encode(req.getPassword())); // NoOp ならそのままでもOK

        userRepository.insert(user); // ここで userId がセットされる

        // 2. Student を作成
        Student student = new Student();
        student.setUserId(user.getUserId());
        student.setGrade(req.getGrade());
        student.setMyClass(req.getMyClass());
        student.setNumber(req.getNumber());

        studentRepository.insert(student);
    }
}
