package com.mcp.server.tools;

import com.mcp.server.ResourceController;
import com.mcp.server.Resource;
import lombok.AllArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class McpToolProvider {

    private final ResourceController resourceController;
    @Tool(name = "getResources", description = "Get all the Resources")
    String getAllResource() {
        return resourceController.getAllResource();
    }

    @Tool(name = "getResourceById", description = "Get all the resource by Id")
    Resource getResourceById(String id) {
        ResponseEntity<Resource> response = resourceController.getResourceById(id);
        return response.getBody();
    }
}