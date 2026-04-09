package com.stu8317.demo1.controller;

import com.stu8317.demo1.common.Result;
import com.stu8317.demo1.dto.UserDTO;
import com.stu8317.demo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口控制器
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 1. 用户注册接口
     */
    @PostMapping
    public Result<String> register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    /**
     * 2. 用户登录接口
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }

    /**
     * 3. 根据ID查询用户接口
     */
    @GetMapping("/{id}")
    public Result<String> getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }
}