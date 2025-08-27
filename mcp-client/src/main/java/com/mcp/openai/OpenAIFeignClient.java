package com.mcp.openai;

import com.mcp.openai.chat.request.ChatApiRequest;
import com.mcp.openai.chat.response.ChatApiResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "openai", url = "https://api.openai.com/v1")
public interface OpenAIFeignClient {

    @PostMapping("/chat/completions")
    @Headers({
            "Content-Type: application/json"
    })
    ChatApiResponse createChatCompletion(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ChatApiRequest request);
}
