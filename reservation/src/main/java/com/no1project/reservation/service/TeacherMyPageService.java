package com.no1project.reservation.service;

import com.no1project.reservation.dto.TeacherMeResponse;
import com.no1project.reservation.model.Teacher;
import com.no1project.reservation.model.User;
import com.no1project.reservation.repository.TeacherRepository;
import com.no1project.reservation.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeacherMyPageService {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    public TeacherMyPageService(UserRepository userRepository,
                                TeacherRepository teacherRepository,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public TeacherMeResponse getMe(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが存在しません。"));

        Teacher teacher = teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("教師情報が存在しません。"));

        TeacherMeResponse res = new TeacherMeResponse();
        res.setUserId(user.getUserId());
        res.setName(user.getName());
        res.setRole(user.getRole());
        res.setEmail(user.getEmail());
        res.setGrade(teacher.getGrade());
        res.setMyClass(teacher.getMyClass());
        return res;
    }

    @Transactional
    public void updateMyEmail(int userId, String newEmail) {
        if (newEmail == null || newEmail.isBlank()) {
            throw new IllegalArgumentException("メールアドレスが空です。");
        }
        if (userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("このメールアドレスはすでに登録されています。");
        }
        int updated = userRepository.updateEmail(userId, newEmail);
        if (updated != 1) {
            throw new IllegalStateException("メール更新に失敗しました。");
        }
    }

    @Transactional
    public void updateMyPassword(int userId, String currentPassword, String newPassword) {
        if (currentPassword == null || currentPassword.isBlank()) {
            throw new IllegalArgumentException("現在のパスワードが空です。");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("新しいパスワードが空です。");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが存在しません。"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("現在のパスワードが正しくありません。");
        }

        String encoded = passwordEncoder.encode(newPassword);
        int updated = userRepository.updatePassword(userId, encoded);
        if (updated != 1) {
            throw new IllegalStateException("パスワード更新に失敗しました。");
        }
    }
}
