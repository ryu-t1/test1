package com.no1project.reservation.service;

import com.no1project.reservation.model.SampleUser;
import com.no1project.reservation.repository.SampleUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SampleUserService {

    private final SampleUserRepository userRepository;

    public SampleUserService(SampleUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<SampleUser> getAllUsers() {
        return userRepository.findAll();
    }

    public SampleUser getUserById(int id) {
        return userRepository.findById(id);
    }
}
