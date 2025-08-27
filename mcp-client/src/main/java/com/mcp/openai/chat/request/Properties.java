package com.mcp.openai.chat.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Properties {

        private String type;        // e.g., "string", "integer"
        private String description; // Argument description
}
