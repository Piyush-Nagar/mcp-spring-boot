package com.mcp.server;

import lombok.AllArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class McpControllerExample {

    private final ResourceController resourceController;
    @Tool(name = "getResources", description = "Get all the Resources")
    String getAllResource() {
        return resourceController.getAllResource();
    }

    @Tool(name = "getResourceById", description = "Get all the resource by Id")
    Response getResourceById(String id) {
        ResponseEntity<Response> response = resourceController.getResourceById(id);
        return response.getBody();
    }

}