package com.stu8317.demo1.vo;

import lombok.Data;

@Data
public class UserDetailVO {
    private Long userId;
    private String username;
    private String realName;
    private String phone;
    private String address;
}