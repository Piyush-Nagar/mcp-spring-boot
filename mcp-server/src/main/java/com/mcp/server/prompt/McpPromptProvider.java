package com.mcp.server.prompt;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class McpPromptProvider {

    public static McpServerFeatures.SyncPromptSpecification getPrompt1() {
        McpSchema.PromptArgument promptArgument = new McpSchema.PromptArgument("arg1", "Argument Arg1",  true);
        McpSchema.Prompt resource = new McpSchema.Prompt("Example Prompt 1", "This is prompt for Example Prompt 1", Collections.singletonList(promptArgument));
        McpServerFeatures.SyncPromptSpecification syncPromptSpecification = new McpServerFeatures.SyncPromptSpecification(resource, new BiFunction<McpSyncServerExchange, McpSchema.GetPromptRequest, McpSchema.GetPromptResult>() {

            @Override
            public McpSchema.GetPromptResult apply(McpSyncServerExchange mcpSyncServerExchange, McpSchema.GetPromptRequest getPromptRequest) {
                Map<String, Object> args = getPromptRequest.arguments();
                String promptContent = MessageFormat.format("I want to know in short about: {0}", args.get("arg1"));
                McpSchema.Content content = new McpSchema.TextContent(promptContent);
                McpSchema.Role role = McpSchema.Role.ASSISTANT;
                McpSchema.PromptMessage message = new McpSchema.PromptMessage(role, content);

                List<McpSchema.PromptMessage> messages = Collections.singletonList(message);
                return new McpSchema.GetPromptResult("Here is prompt for Example Prompt 1", messages);

            }
        });
        return syncPromptSpecification;
    }

    public static McpServerFeatures.SyncPromptSpecification getPrompt2() {
        McpSchema.PromptArgument promptArgument = new McpSchema.PromptArgument("arg1", "Argument Arg1",  true);
        McpSchema.Prompt resource = new McpSchema.Prompt("Example Prompt 2", "This is prompt for Example Prompt 2", Collections.singletonList(promptArgument));
        McpServerFeatures.SyncPromptSpecification syncPromptSpecification = new McpServerFeatures.SyncPromptSpecification(resource, new BiFunction<McpSyncServerExchange, McpSchema.GetPromptRequest, McpSchema.GetPromptResult>() {
            @Override
            public McpSchema.GetPromptResult apply(McpSyncServerExchange mcpSyncServerExchange, McpSchema.GetPromptRequest getPromptRequest) {
                Map<String, Object> args = getPromptRequest.arguments();
                String promptContent = MessageFormat.format("I want to know in detail about: {0}", args.get("arg1"));
                McpSchema.Content content = new McpSchema.TextContent(promptContent);
                McpSchema.Role role = McpSchema.Role.ASSISTANT;
                McpSchema.PromptMessage message = new McpSchema.PromptMessage(role, content);

                List<McpSchema.PromptMessage> messages = Collections.singletonList(message);
                return new McpSchema.GetPromptResult("Here is prompt for Example Prompt 2", messages);
            }
        });
        return syncPromptSpecification;
    }
}
