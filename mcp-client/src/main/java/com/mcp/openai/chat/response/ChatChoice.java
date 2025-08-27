package com.mcp.openai.chat.response;

import com.mcp.openai.chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatChoice {

    private int index;

    private String finish_reason;
    private ChatMessage message;

}
