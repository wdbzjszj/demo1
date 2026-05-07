package com.stu8317.demo1.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stu8317.demo1.common.Result;
import com.stu8317.demo1.dto.UserDTO;
import com.stu8317.demo1.entity.User;
import com.stu8317.demo1.entity.UserInfo;
import com.stu8317.demo1.vo.UserDetailVO;

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

    // 新增：根据用户ID查询详情（多表联查 + Redis缓存）
    Result<UserDetailVO> getUserDetail(Long userId);

    // 新增：更新用户扩展信息
    Result<String> updateUserInfo(UserInfo userInfo);

    // 新增：删除用户（带缓存清除）
    Result<String> deleteUser(Long userId);

    Result<User> getCurrentUserInfo();

}//