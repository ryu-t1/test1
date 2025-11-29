package com.no1project.reservation.controller; //テスト用です。

import com.no1project.reservation.model.SampleUser;
import com.no1project.reservation.service.SampleUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SampleUserController {

    private final SampleUserService userService;

    public SampleUserController(SampleUserService userService) {
        this.userService = userService;
    }

    // 获取所有用户
    @GetMapping("/users")
    public List<SampleUser> getAllUsers() {
        return userService.getAllUsers();
    }

    // 根据ID获取单个用户
    @GetMapping("/users/{id}")
    public SampleUser getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }
}
