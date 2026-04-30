package com.stu8317.demo1.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("users") // 对应你数据库里的表名
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private LocalDateTime createdAt = LocalDateTime.now();
}