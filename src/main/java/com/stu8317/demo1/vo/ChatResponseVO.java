package com.stu8317.demo1.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseVO {
    // 用户的问题
    private String question;
    // 大模型的回答
    private String answer;
}