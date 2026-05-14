package com.stu8317.demo1.service;

import com.stu8317.demo1.dto.ChatRequestDTO;
import com.stu8317.demo1.vo.ChatResponseVO;

public interface ChatService {
    ChatResponseVO chat(ChatRequestDTO requestDTO);
}