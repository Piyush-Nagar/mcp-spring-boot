package com.mcp.mcpresponse.result.prompt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class McpPrompt {

    private String name;
    private String description;
    private List<McpPromptArgument> arguments;
}
