package com.mcp.openai;


import com.mcp.openai.chat.request.ChatApiRequest;
import com.mcp.openai.chat.response.ChatApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OpenAIClient {

    private OpenAIFeignClient feignClient;
    String authorization = "{Add your Open AI Auth Key}";

    public OpenAIClient(OpenAIFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    public ChatApiResponse createChatCompletion(ChatApiRequest chatApiRequest) {
        return feignClient.createChatCompletion(authorization, chatApiRequest);
    }

}
