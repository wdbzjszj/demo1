package com.stu8317.demo1.controller;

import com.stu8317.demo1.common.Result;
import com.stu8317.demo1.dto.UserDTO;
import com.stu8317.demo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 1. 用户注册 - POST /api/users
    @PostMapping
    public Result<String> register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    // 2. 用户登录 - POST /api/users/login
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }

    // 3. 测试接口 - GET /api/users/{id}
    @GetMapping("/{id}")
    public Result<String> getUser(@PathVariable("id") Long id) {
        return Result.success("查询成功，正在返回 ID 为 " + id + " 的用户信息");
    }
}