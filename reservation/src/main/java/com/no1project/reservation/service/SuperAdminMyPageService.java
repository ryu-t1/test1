package com.no1project.reservation.service;

import com.no1project.reservation.dto.SuperAdminMeResponse;
import com.no1project.reservation.model.User;
import com.no1project.reservation.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class SuperAdminMyPageService {

    private final UserRepository userRepository;

    public SuperAdminMyPageService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SuperAdminMeResponse getMe(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが存在しません。"));

        SuperAdminMeResponse res = new SuperAdminMeResponse();
        res.setUserId(user.getUserId());
        res.setName(user.getName());
        res.setRole(user.getRole());
        res.setEmail(user.getEmail());
        return res;
    }
}
