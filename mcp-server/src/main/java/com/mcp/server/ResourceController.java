package com.mcp.server;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

    @GetMapping("/resources")
    public String getAllResource() {
        return "MCP Server is running!";
    }

    @GetMapping("/resources/{id}")
    public ResponseEntity<Response> getResourceById(@PathVariable("id") String id) {
        Response response = Response.builder().id("1").name("Name").build();
        return ResponseEntity.ok(response);
    }
}
