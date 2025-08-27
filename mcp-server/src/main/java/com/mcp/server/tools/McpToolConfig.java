package com.mcp.server.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class McpToolConfig {
    private final McpToolProvider mcpToolProvider;
    @Bean
    ToolCallbackProvider userTools() {
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(mcpToolProvider)
                .build();
    }
}