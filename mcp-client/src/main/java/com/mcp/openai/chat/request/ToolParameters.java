package com.mcp.openai.chat.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolParameters {

    private String type = "object"; // Usually "object" for parameter schemas
    private Map<String, Properties> properties; // Key = argument name
    private List<String> required; // Required argument names
}
