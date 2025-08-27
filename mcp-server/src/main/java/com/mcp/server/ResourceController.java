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
    public ResponseEntity<Resource> getResourceById(@PathVariable("id") String id) {
        Resource resource = Resource.builder().id("1").name("Piyush  Nagar")
                .fathersName("Lalit Nagar")
                .mothersName("Madhubala")
                .age("36")
                .build();
        return ResponseEntity.ok(resource);
    }
}
