package com.mcp.mcpresponse.result.prompt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class McpPromptArgument {

     private String name;
     private String description;
     private String required;
}
