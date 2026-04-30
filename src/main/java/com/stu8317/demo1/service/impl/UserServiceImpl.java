package com.stu8317.demo1.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stu8317.demo1.common.Result;
import com.stu8317.demo1.common.ResultCode;
import com.stu8317.demo1.dto.UserDTO;
import com.stu8317.demo1.entity.User;
import com.stu8317.demo1.entity.UserInfo;
import com.stu8317.demo1.mapper.UserMapper;
import com.stu8317.demo1.mapper.UserInfoMapper;
import com.stu8317.demo1.security.JwtUtil;
import com.stu8317.demo1.service.UserService;
import com.stu8317.demo1.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil; // 注入JwtUtil

    private static final String CACHE_KEY_PREFIX = "user:detail:";

    // ===================== 原有方法（保留，仅修改login） =====================
    @Override
    public Result<String> register(UserDTO userDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername());
        User dbUser = userMapper.selectOne(queryWrapper);
        if (dbUser != null) {
            return Result.error(ResultCode.USER_HAS_EXISTED);
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userMapper.insert(user);
        return Result.success("注册成功！");
    }

    @Override
    public Result<String> login(UserDTO userDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername());
        User dbUser = userMapper.selectOne(queryWrapper);
        if (dbUser == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        if (!passwordEncoder.matches(userDTO.getPassword(), dbUser.getPassword())) {
            return Result.error(ResultCode.PASSWORD_ERROR);
        }
        // 登录成功，生成JWT并返回
        String jwt = jwtUtil.generateToken(userDTO.getUsername());
        return Result.success(jwt);
    }

    // 其他方法（getUserById、getUserPage等）完全保留，无需修改
    @Override
    public Result<String> getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success("查询成功，用户信息：" + user.toString());
    }

    @Override
    public Result<Page<User>> getUserPage(Integer pageNum, Integer pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        userMapper.selectPage(page, null);
        return Result.success(page);
    }

    @Override
    public Result<UserDetailVO> getUserDetail(Long userId) {
        String key = CACHE_KEY_PREFIX + userId;
        String json = redisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(json)) {
            try {
                UserDetailVO cacheVO = JSONUtil.toBean(json, UserDetailVO.class);
                return Result.success(cacheVO);
            } catch (Exception e) {
                redisTemplate.delete(key);
            }
        }
        UserDetailVO detail = userInfoMapper.getUserDetail(userId);
        if (detail == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        redisTemplate.opsForValue().set(
                key,
                JSONUtil.toJsonStr(detail),
                10,
                TimeUnit.MINUTES
        );
        return Result.success(detail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updateUserInfo(UserInfo userInfo) {
        if (userInfo == null || userInfo.getUserId() == null) {
            return Result.error("参数错误");
        }
        userInfoMapper.updateById(userInfo);
        String key = CACHE_KEY_PREFIX + userInfo.getUserId();
        redisTemplate.delete(key);
        return Result.success("更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteUser(Long userId) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }
        userMapper.deleteById(userId);
        userInfoMapper.delete(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUserId, userId));
        String key = CACHE_KEY_PREFIX + userId;
        redisTemplate.delete(key);
        return Result.success("删除成功");
    }
}