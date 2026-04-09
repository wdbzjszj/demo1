package com.stu8317.demo1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stu8317.demo1.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口，继承MyBatis-Plus的BaseMapper，内置CRUD方法
 */
@Mapper // 标识为MyBatis Mapper，交由Spring管理
public interface UserMapper extends BaseMapper<User> {
    // 无需手动写CRUD，BaseMapper已内置
}