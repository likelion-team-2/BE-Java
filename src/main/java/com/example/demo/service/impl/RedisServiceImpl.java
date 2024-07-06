package com.example.demo.service.impl;

import com.example.demo.dto.response.ChatMessageResponseDto;
import com.example.demo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RedisServiceImpl implements RedisService {

    private static final String SESSION_HASH_KEY = "session_";

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    private HashOperations<Object, String, ChatMessageResponseDto> hashOperations;

    public RedisServiceImpl(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void saveMessage(ChatMessageResponseDto message, String sessionId) {
        hashOperations.put(SESSION_HASH_KEY + sessionId, message.getId(), message);
    }

    @Override
    public Map<String, ChatMessageResponseDto> findMessagesInSession(String sessionId) {
        return hashOperations.entries(SESSION_HASH_KEY + sessionId);
    }

    @Override
    public ChatMessageResponseDto findById(String id, String sessionId) {
        return hashOperations.get(SESSION_HASH_KEY + sessionId, id);
    }

    @Override
    public void update(ChatMessageResponseDto message, String sessionId) {
        this.saveMessage(message, sessionId);
    }

    @Override
    public void delete(String id, String sessionId) {
        hashOperations.delete(SESSION_HASH_KEY + sessionId, id);
    }
}
