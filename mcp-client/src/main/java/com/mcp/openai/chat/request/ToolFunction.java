package com.mcp.openai.chat.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolFunction {

    private String name;
    private String description;
    private ToolParameters parameters;
}
