package com.mcp.mcpresponse.result;

import com.mcp.mcpresponse.result.tool.ToolDefinition;
import com.mcp.mcpresponse.result.content.McpContent;
import com.mcp.mcpresponse.result.prompt.McpPrompt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private List<McpPrompt> prompts;
    private List<ToolDefinition> tools;
    private McpContent content;

}
