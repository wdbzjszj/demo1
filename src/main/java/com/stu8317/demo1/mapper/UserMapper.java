package com.stu8317.demo1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stu8317.demo1.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}