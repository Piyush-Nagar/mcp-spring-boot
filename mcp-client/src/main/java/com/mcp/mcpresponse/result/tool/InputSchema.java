package com.mcp.mcpresponse.result.tool;

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
public class InputSchema {
    private String type;
    private Map<String, Property> properties;
    private List<String> required;
    private boolean additionalProperties;
}
