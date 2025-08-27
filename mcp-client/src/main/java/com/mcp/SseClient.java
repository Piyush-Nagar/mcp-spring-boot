package com.mcp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.mcpresponse.McpToolResponse;
import com.mcp.mcpresponse.result.Result;
import com.mcp.mcpresponse.result.prompt.McpPrompt;
import com.mcp.mcpresponse.result.prompt.McpPromptArgument;
import com.mcp.mcpresponse.result.tool.InputSchema;
import com.mcp.mcpresponse.result.tool.Property;
import com.mcp.mcpresponse.result.tool.ToolDefinition;
import com.mcp.openai.OpenAIClient;
import com.mcp.openai.chat.ChatMessage;
import com.mcp.openai.chat.request.*;
import com.mcp.openai.chat.request.Properties;
import com.mcp.openai.chat.response.ChatApiResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SseClient {

    private final WebClient webClient;
    private final OpenAIClient openAIClient;
    private String sessionId;

    public SseClient(WebClient.Builder webClientBuilder, OpenAIClient openAIClient) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
        this.openAIClient = openAIClient;
    }

    public void startListening() {




        Flux<String> eventStream = webClient.get()
                .uri("/sse")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);
        eventStream.subscribe(
                event -> doSomething(event),
                error -> System.err.println("Error in SSE stream: " + error),
                () -> System.out.println("SSE stream completed."));
    }

    private void doSomething(String event) {
        System.out.println("Received SSE event: " + event);
        String[] split = event.split("=");
        if(split.length == 2 ) {
            this.sessionId = split[1];
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        McpToolResponse mcpToolResponse = null;
        try {
            mcpToolResponse = objectMapper.readValue(event, McpToolResponse.class);
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            return;
        }

        if (Objects.nonNull(mcpToolResponse.getResult())) {
            ChatApiRequest chatApiRequest = mapToOpenAIRequest(mcpToolResponse);
            ChatApiResponse chatApiResponse = openAIClient.createChatCompletion(chatApiRequest);
//            callTool();
            callPrompt();

            System.out.println("OpenAI Response: " + chatApiResponse);
        }
    }

    public static ChatApiRequest mapToOpenAIRequest(McpToolResponse mcpToolResponse) {

        Result result = mcpToolResponse.getResult();
        List<ToolDefinition> tools = result.getTools();

        // Build the property for "location"
//        List<Tool> tool = getToolFromMcpTool(tools);
        List<Tool> tool = getToolFromMcpPrompt(result.getPrompts());

        ChatMessage chatChoiceSystem = ChatMessage.builder()
                .role("system")
                .content("You are a helpful assistant")
                .build();

        ChatMessage chatChoiceUser = ChatMessage.builder()
                .role("user")
                .content("I want to travel to New York")
                .build();

        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(chatChoiceSystem);
        chatMessages.add(chatChoiceUser);

        ChatApiRequest chatApiRequest = ChatApiRequest.builder()
                .model("gpt-4")
                .messages(chatMessages)
                .tools(tool)
                .build();
        return chatApiRequest;
    }

    private static List<Tool> getToolFromMcpTool(List<ToolDefinition> tools) {
        List<Tool> collect = tools.stream().map(toolDefinition -> {
            InputSchema inputSchema = toolDefinition.getInputSchema();
            Map<String, Property> properties1 = inputSchema.getProperties();

            ArrayList<String> required = new ArrayList<>();
            Map<String, Properties> props = properties1.entrySet()
                    .stream()
                    .map(x -> {
                        Properties properties = Properties.builder()
                                .type(x.getValue().getType())
                                .description("Fields")
                                .build();
                        required.add(x.getKey());
                        Map.Entry<String, Properties> entry =
                                new AbstractMap.SimpleEntry<>(x.getKey(), properties);
                        return entry;
                    })
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

            ToolParameters params = ToolParameters.builder()
                    .type("object")
                    .properties(props)
                    .required(required)
                    .build();

            ToolFunction func = ToolFunction.builder()
                    .name(toolDefinition.getName())
                    .description(toolDefinition.getDescription())
                    .parameters(params)
                    .build();

            Tool tool = Tool.builder()
                    .type("function")
                    .function(func)
                    .build();
            return tool;
        }).collect(Collectors.toList());

        return collect;

    }

    private static List<Tool> getToolFromMcpPrompt(List<McpPrompt> prompts) {
        List<Tool> collect = prompts.stream().map(prompt -> {
            List<McpPromptArgument> arguments = prompt.getArguments();

            ArrayList<String> required = new ArrayList<>();
            Map<String, Properties> props = arguments
                    .stream()
                    .map(argument -> {
                        Properties properties = Properties.builder()
                                .type("string")
                                .description(argument.getDescription())
                                .build();
                        required.add(argument.getName());
                        Map.Entry<String, Properties> entry =
                                new AbstractMap.SimpleEntry<>(argument.getName(), properties);
                        return entry;
                    })
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

            ToolParameters params = ToolParameters.builder()
                    .type("object")
                    .properties(props)
                    .required(required)
                    .build();

            ToolFunction func = ToolFunction.builder()
                    .name(prompt.getName())
                    .description(prompt.getDescription())
                    .parameters(params)
                    .build();

            Tool tool = Tool.builder()
                    .type("function")
                    .function(func)
                    .build();
            return tool;
        }).collect(Collectors.toList());

        return collect;

    }

    public void startflow(String token) {
        sendInitializeRequest(token);
        sendInitializeAck(token);
//        sendToolList(token);
        sendPromptList(token);

    }
    public void sendInitializeRequest(String sessionId) {
        String uri = "/mcp/message?sessionId=" + sessionId;

        // Create the request payload as a Java object or a raw map
        String jsonBody = """
            {
              "jsonrpc": "2.0",
              "id": 1,
              "method": "initialize",
              "params": {
                "protocolVersion": "2024-11-05",
                "capabilities": {
                  "roots": {
                    "listChanged": true
                  },
                  "sampling": {}
                },
                "clientInfo": {
                  "name": "ExampleClient",
                  "version": "1.0.0"
                }
              }
            }
            """;

        webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonBody)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Response: " + response))
                .doOnError(error -> System.err.println("Error: " + error.getMessage()))
                .subscribe();
    }

    public void sendInitializeAck(String sessionId) {
        String uri = "/mcp/message?sessionId=" + sessionId;

        // Create the request payload as a Java object or a raw map
        String jsonBody = """
                {
                  "jsonrpc": "2.0",
                  "method": "notifications/initialized"
                }
                """;

        webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonBody)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Response: " + response))
                .doOnError(error -> System.err.println("Error: " + error.getMessage()))
                .subscribe();
    }

    public void sendToolList(String sessionId) {
        String uri = "/mcp/message?sessionId=" + sessionId;

        // Create the request payload as a Java object or a raw map
        String jsonBody = """
                {
                  "jsonrpc": "2.0",
                   "id": "2",
                   "method": "tools/list"
                }
                """;

        webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonBody)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Response: " + response))
                .doOnError(error -> System.err.println("Error: " + error.getMessage()))
                .subscribe();
    }

    public void sendPromptList(String sessionId) {
        String uri = "/mcp/message?sessionId=" + sessionId;

        // Create the request payload as a Java object or a raw map
        String jsonBody = """
                {
                  "jsonrpc": "2.0",
                   "id": "2",
                   "method": "prompts/list"
                }
                """;

        webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonBody)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Response: " + response))
                .doOnError(error -> System.err.println("Error: " + error.getMessage()))
                .subscribe();
    }

    public void callTool() {
        String uri = "/mcp/message?sessionId=" + sessionId;

        // Create the request payload as a Java object or a raw map
        String jsonBody = """
                {
                  "jsonrpc": "2.0",
                  "id": 2,
                  "method": "tools/call",
                  "params": {
                    "name": "getResourceById",
                    "arguments": {
                      "id": "1234"
                    }
                  }
                }
                """;

        webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonBody)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Response: " + response))
                .doOnError(error -> System.err.println("Error: " + error.getMessage()))
                .subscribe();
    }

    public void callPrompt() {
        String uri = "/mcp/message?sessionId=" + sessionId;

        // Create the request payload as a Java object or a raw map
        String jsonBody = """
                {
                  "jsonrpc": "2.0",
                  "id": 2,
                  "method": "prompts/get",
                  "params": {
                    "name": "Travel_Guide_Prompt",
                    "arguments": {
                      "location": "New York"
                    }
                  }
                }
                """;

        webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonBody)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Response: " + response))
                .doOnError(error -> System.err.println("Error: " + error.getMessage()))
                .subscribe();
    }
}
