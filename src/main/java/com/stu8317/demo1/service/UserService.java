package com.stu8317.demo1.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stu8317.demo1.common.Result;
import com.stu8317.demo1.dto.UserDTO;
import com.stu8317.demo1.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     */
    Result<String> register(UserDTO userDTO);

    /**
     * 用户登录
     */
    Result<String> login(UserDTO userDTO);

    /**
     * 根据ID查询用户
     */
    Result<String> getUserById(Long id);

    /**
     * 分页查询用户
     */
    Result<Page<User>> getUserPage(Integer pageNum, Integer pageSize);
}