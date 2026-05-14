package com.stu8317.demo1.service.impl;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.stu8317.demo1.dto.ChatRequestDTO;
import com.stu8317.demo1.service.ChatService;
import com.stu8317.demo1.vo.ChatResponseVO;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;
    private final StringRedisTemplate stringRedisTemplate;

    public ChatServiceImpl(ChatClient.Builder chatClientBuilder,
                           StringRedisTemplate stringRedisTemplate) {
        this.chatClient = chatClientBuilder
                .defaultSystem("你是一名专业、友好、简洁的中文智能助手，请结合历史对话上下文回答用户问题。")
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withTopP(0.7)
                                .build()
                )
                .build();
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public ChatResponseVO chat(ChatRequestDTO requestDTO) {
        String sessionId = requestDTO.getSessionId();
        String message = requestDTO.getMessage();

        // 校验 sessionId 和 message 非空
        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("sessionId 不能为空");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message 不能为空");
        }

        String redisKey = "chat:session:" + sessionId;

        // 1. 读取历史消息（最多取最近3轮）
        List<String> records = stringRedisTemplate.opsForList().range(redisKey, 0, -1);
        StringBuilder historyText = new StringBuilder();
        if (records != null && !records.isEmpty()) {
            for (String record : records) {
                historyText.append(record).append("\n");
            }
        }

        // 2. 拼接上下文，构建最终 Prompt
        String finalPrompt = String.format("""
                以下是历史对话：
                %s
                当前用户问题：
                %s
                """, historyText, message);

        // 3. 调用模型
        String answer = chatClient.prompt(finalPrompt)
                .call()
                .content();

        // 4. 保存本轮记录到 Redis
        String recordText = "用户：" + message + "\n助手：" + answer;
        stringRedisTemplate.opsForList().rightPush(redisKey, recordText);

        // 5. 只保留最近 3 轮对话，避免上下文过长
        Long size = stringRedisTemplate.opsForList().size(redisKey);
        if (size != null && size > 3) {
            stringRedisTemplate.opsForList().trim(redisKey, size - 3, size - 1);
        }

        // 6. 返回结果
        return new ChatResponseVO(message, answer);
    }
}