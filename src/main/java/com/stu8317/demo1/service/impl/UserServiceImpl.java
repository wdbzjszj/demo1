package com.stu8317.demo1.service.impl;

import com.stu8317.demo1.common.Result;
import com.stu8317.demo1.common.ResultCode;
import com.stu8317.demo1.dto.UserDTO;
import com.stu8317.demo1.entity.User;
import com.stu8317.demo1.mapper.UserMapper;
import com.stu8317.demo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<String> register(UserDTO userDTO) {
        // 1. 校验用户是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userDTO.getUsername());
        User existUser = userMapper.selectOne(wrapper);
        if (existUser != null) {
            return Result.error(ResultCode.USER_HAS_EXISTED);
        }
        // 2. 插入用户
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        userMapper.insert(user);
        return Result.success("注册成功");
    }

    @Override
    public Result<String> login(UserDTO userDTO) {
        // 1. 校验用户是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userDTO.getUsername());
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        // 2. 校验密码是否正确
        if (!user.getPassword().equals(userDTO.getPassword())) {
            return Result.error(ResultCode.PASSWORD_ERROR);
        }
        return Result.success("登录成功");
    }
}