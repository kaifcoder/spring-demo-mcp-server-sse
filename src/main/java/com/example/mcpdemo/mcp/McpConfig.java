package com.example.mcpdemo.mcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.mcpdemo.service.UserService;


@Configuration
public class McpConfig {
    /**
     * Configures the MCP server to expose tools from the UserService.
     *
     * @param userService the UserService instance
     * @return a ToolCallbackProvider that provides access to UserService tools
     */

     
    @Bean
    public ToolCallbackProvider userTools(UserService userService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(userService)
                .build();
    }
}
