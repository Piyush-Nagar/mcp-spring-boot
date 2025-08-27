package com.mcp;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class McpClientController {
    private final SseClient sseClient;

    @GetMapping("/mcp-clients")
    public String getAllResource() {
        sseClient.startListening();
        return "MCP Server is running!";
    }

    @GetMapping("/mcp-clients/start-flow")
    public String initialise(@RequestParam(required = false) String token) {
        sseClient.startflow(token);
        return "MCP Server is running!";
    }
}
