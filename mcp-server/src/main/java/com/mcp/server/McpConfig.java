package com.mcp.server;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Configuration
@RequiredArgsConstructor
public class McpConfig {

    private final McpControllerExample mcpControllerExample;

    @Bean
    ToolCallbackProvider userTools() {
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(mcpControllerExample)
                .build();
    }

    @Bean
    public List<McpServerFeatures.SyncResourceSpecification> myResources() {
        McpSchema.Resource resource = new McpSchema.Resource("custom://resource", "name", "description", "mime-type", null);
        McpServerFeatures.SyncResourceSpecification syncResourceSpecification = new McpServerFeatures.SyncResourceSpecification(resource, new BiFunction<McpSyncServerExchange, McpSchema.ReadResourceRequest, McpSchema.ReadResourceResult>() {

            @Override
            public McpSchema.ReadResourceResult apply(McpSyncServerExchange mcpSyncServerExchange, McpSchema.ReadResourceRequest readResourceRequest) {
                McpSchema.ResourceContents systemInfo = new McpSchema.TextResourceContents("custom://resource", "name", "description");
                return new McpSchema.ReadResourceResult(Collections.singletonList(systemInfo));
            }
        });

        return Collections.singletonList(syncResourceSpecification);
    }


    @Bean
    public List<McpServerFeatures.SyncPromptSpecification> myPrompts() {
        McpServerFeatures.SyncPromptSpecification syncPromptSpecification = getPrompt1();
        McpServerFeatures.SyncPromptSpecification syncPromptSpecification2 = getPrompt2();
        return Arrays.asList(syncPromptSpecification, syncPromptSpecification2);
    }

    private static McpServerFeatures.SyncPromptSpecification getPrompt2() {
        McpSchema.PromptArgument promptArgument = new McpSchema.PromptArgument("name", "Student Name",  true);
        McpSchema.Prompt resource = new McpSchema.Prompt("LeaveApplication", "This prompt will be for Leave application of Student", Collections.singletonList(promptArgument));
        McpServerFeatures.SyncPromptSpecification syncPromptSpecification = new McpServerFeatures.SyncPromptSpecification(resource, new BiFunction<McpSyncServerExchange, McpSchema.GetPromptRequest, McpSchema.GetPromptResult>() {

            @Override
            public McpSchema.GetPromptResult apply(McpSyncServerExchange mcpSyncServerExchange, McpSchema.GetPromptRequest getPromptRequest) {
                Map<String, Object> args = getPromptRequest.arguments();
                String promptContent = MessageFormat.format("Write a Leave application for Student with name: {0}", args.get("name"));
                McpSchema.Content content = new McpSchema.TextContent(promptContent);
                McpSchema.Role role = McpSchema.Role.ASSISTANT;
                McpSchema.PromptMessage message = new McpSchema.PromptMessage(role, content);

                List<McpSchema.PromptMessage> messages = Collections.singletonList(message);
                return new McpSchema.GetPromptResult("Here is prompt for Student Leave", messages);

            }
        });
        return syncPromptSpecification;
    }

    private static McpServerFeatures.SyncPromptSpecification getPrompt1() {
        McpSchema.PromptArgument promptArgument = new McpSchema.PromptArgument("location", "location for visit",  true);
        McpSchema.Prompt resource = new McpSchema.Prompt("Travel_Guide_Prompt", "This prompt template provides travel guide", Collections.singletonList(promptArgument));
        McpServerFeatures.SyncPromptSpecification syncPromptSpecification = new McpServerFeatures.SyncPromptSpecification(resource, new BiFunction<McpSyncServerExchange, McpSchema.GetPromptRequest, McpSchema.GetPromptResult>() {

            @Override
            public McpSchema.GetPromptResult apply(McpSyncServerExchange mcpSyncServerExchange, McpSchema.GetPromptRequest getPromptRequest) {
                Map<String, Object> args = getPromptRequest.arguments();
                String promptContent = MessageFormat.format("Write a travel guide to reach: {0}", args.get("location"));
                McpSchema.Content content = new McpSchema.TextContent("Here is the information for: " + promptContent);
                McpSchema.Role role = McpSchema.Role.ASSISTANT;
                McpSchema.PromptMessage message = new McpSchema.PromptMessage(role, content);

                List<McpSchema.PromptMessage> messages = Collections.singletonList(message);
                return new McpSchema.GetPromptResult("Here is Prompt for Travel guide for your destination", messages);
            }
        });
        return syncPromptSpecification;
    }

}