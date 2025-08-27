package com.mcp.server.resource;

import io.modelcontextprotocol.server.McpServerFeatures;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

import static com.mcp.server.resource.McpResourceProvider.getSyncResourceSpecification;

@Configuration
public class McpResourceConfig {

    @Bean
    public List<McpServerFeatures.SyncResourceSpecification> resourceBeans() {
        McpServerFeatures.SyncResourceSpecification syncResourceSpecification = getSyncResourceSpecification();
        return Collections.singletonList(syncResourceSpecification);
    }
}
