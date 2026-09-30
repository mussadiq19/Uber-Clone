package com.example.ubersockerservice.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    private String name;
    private String message;
}
