package com.mcp.openai.chat.request;

import com.mcp.openai.chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatApiRequest {

    private String model;
    private List<ChatMessage> messages;
    private List<Tool> tools;

}
