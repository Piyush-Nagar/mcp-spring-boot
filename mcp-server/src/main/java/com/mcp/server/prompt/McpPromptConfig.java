package com.mcp.server.prompt;

import io.modelcontextprotocol.server.McpServerFeatures;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

import static com.mcp.server.prompt.McpPromptProvider.getPrompt1;
import static com.mcp.server.prompt.McpPromptProvider.getPrompt2;

@Configuration
public class McpPromptConfig {

    @Bean
    public List<McpServerFeatures.SyncPromptSpecification> promptBeans() {
        McpServerFeatures.SyncPromptSpecification syncPromptSpecification = getPrompt1();
        McpServerFeatures.SyncPromptSpecification syncPromptSpecification2 = getPrompt2();
        return Arrays.asList(syncPromptSpecification, syncPromptSpecification2);
    }

}