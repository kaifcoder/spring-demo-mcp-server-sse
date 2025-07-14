package com.example.mcpdemo.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
    
    public String getUserDetails(String userId) {
        return "User: " + userId + ", Name: John Doe";
    }

    @Tool(description = "Get user details by user ID")
    public String getUserDetailsMcp(String userId) {
        log.debug("Called getUserDetailsMcp with userId: {}", userId);
        return getUserDetails(userId);
    }
}
