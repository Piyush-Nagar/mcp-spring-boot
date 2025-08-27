package com.mcp.server;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {

    private String id;
    private String name;
    private String fathersName;
    private String mothersName;
    private String age;
}
