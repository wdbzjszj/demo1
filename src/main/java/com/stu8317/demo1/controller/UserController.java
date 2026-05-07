package com.stu8317.demo1.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stu8317.demo1.common.Result;
import com.stu8317.demo1.dto.UserDTO;
import com.stu8317.demo1.entity.User;
import com.stu8317.demo1.entity.UserInfo;
import com.stu8317.demo1.service.UserService;
import com.stu8317.demo1.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ===================== 原有接口（保留） =====================

    /**
     * 获取当前登录用户信息（从JWT中读取）
     */
    @GetMapping("/info")
    public Result<User> getCurrentUserInfo() {
        return userService.getCurrentUserInfo();
    }

    @PostMapping
    public Result<String> register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }

    @GetMapping("/{id}")
    public Result<String> getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/page")
    public Result<Page<User>> getUserPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize
    ) {
        return userService.getUserPage(pageNum, pageSize);
    }

    // ===================== 任务7新增接口 =====================
    /**
     * 查询用户详情（多表联查 + Redis缓存）
     */
    @GetMapping("/{id}/detail")
    public Result<UserDetailVO> getUserDetail(@PathVariable("id") Long id) {
        return userService.getUserDetail(id);
    }

    /**
     * 更新用户扩展信息
     */
    @PutMapping("/{id}/detail")
    public Result<String> updateUserInfo(
            @PathVariable("id") Long userId,
            @RequestBody UserInfo userInfo
    ) {
        userInfo.setUserId(userId);
        return userService.updateUserInfo(userInfo);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable("id") Long userId) {
        return userService.deleteUser(userId);
    }

}