package com.mcp.openai.chat.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tool {
    private String type;       // Tool identifier
    private ToolFunction function; // MCP server URL
}
