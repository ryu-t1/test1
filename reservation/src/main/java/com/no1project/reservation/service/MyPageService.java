package com.no1project.reservation.service;

import com.no1project.reservation.dto.MeResponse;
import com.no1project.reservation.repository.StudentRepository;
import com.no1project.reservation.repository.UserRepository;
import com.no1project.reservation.model.Student;
import com.no1project.reservation.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MyPageService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public MyPageService(UserRepository userRepository,
                         StudentRepository studentRepository,
                         PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MeResponse getMe(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが存在しません。"));

        // 学生情報（学生以外でも将来使えるよう、無ければ null でもいいが、今回は学生前提）
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("学生情報が存在しません。"));

        MeResponse res = new MeResponse();
        res.setUserId(user.getUserId());
        res.setName(user.getName());
        res.setRole(user.getRole());
        res.setEmail(user.getEmail());
        res.setGrade(student.getGrade());
        res.setMyClass(student.getMyClass());
        res.setNumber(student.getNumber());
        return res;
    }

    @Transactional
    public void updateMyEmail(int userId, String newEmail) {
        if (newEmail == null || newEmail.isBlank()) {
            throw new IllegalArgumentException("メールアドレスが空です。");
        }
        // 自分の現在メールと同じならOKでもいいが、基本は更新不要として弾く方が親切
        // ただし重複チェックは必須
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

        // 現在パスワード照合（NoOpでも一応この書き方でOK）
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
