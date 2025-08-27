package com.mcp.mcpresponse;

import com.mcp.mcpresponse.result.Result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class McpToolResponse {
    private String jsonrpc;
    private int id;
    private Result result;
}

