package com.mcp.server.resource;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class McpResourceProvider {

    public static McpServerFeatures.SyncResourceSpecification getSyncResourceSpecification() {
        McpSchema.Resource resource = new McpSchema.Resource("custom://resource", "name", "description", "mime-type", null);
        McpServerFeatures.SyncResourceSpecification syncResourceSpecification = new McpServerFeatures.SyncResourceSpecification(resource, new BiFunction<McpSyncServerExchange, McpSchema.ReadResourceRequest, McpSchema.ReadResourceResult>() {
            @Override
            public McpSchema.ReadResourceResult apply(McpSyncServerExchange mcpSyncServerExchange, McpSchema.ReadResourceRequest readResourceRequest) {
                McpSchema.ResourceContents systemInfo = new McpSchema.TextResourceContents("custom://resource", "name", "description");
                return new McpSchema.ReadResourceResult(Collections.singletonList(systemInfo));
            }
        });
        return syncResourceSpecification;
    }
}
