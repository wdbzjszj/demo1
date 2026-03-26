package com.stu8317.demo1.service;

import com.stu8317.demo1.common.Result;
import com.stu8317.demo1.dto.UserDTO;

public interface UserService {
    Result<String> register(UserDTO userDTO);
    Result<String> login(UserDTO userDTO);
}