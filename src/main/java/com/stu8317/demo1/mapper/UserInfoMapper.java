package com.stu8317.demo1.mapper;

import com.stu8317.demo1.entity.UserInfo; // 导入UserInfo实体类
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stu8317.demo1.vo.UserDetailVO; // 导入UserDetailVO
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserInfoMapper extends BaseMapper<UserInfo> {

    @Select("""
            SELECT u.id AS userId,
                   u.username,
                   i.real_name AS realName,
                   i.phone,
                   i.address
            FROM sys_user u
            LEFT JOIN user_info i ON u.id = i.user_id
            WHERE u.id = #{userId}
            """)
    UserDetailVO getUserDetail(@Param("userId") Long userId);
}