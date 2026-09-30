package com.example.ubersockerservice.controllers;

import com.example.ubersockerservice.dtos.ChatRequest;
import com.example.ubersockerservice.dtos.ChatResponse;
import com.example.ubersockerservice.dtos.TestRequest;
import com.example.ubersockerservice.dtos.TestResponse;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public TestController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/ping")//this means client can send req to /ping
    @SendTo("/topic/ping")
    public TestResponse pingCheck(TestRequest message) {
        System.out.println("Received message from client" + message.getData());
        return TestResponse.builder().data("Received").build();
    }
    @Scheduled(fixedDelay = 2000)
    public void sendPeriodicMessage(){
        simpMessagingTemplate.convertAndSend( "/topic/scheduled","Periodic message from the server " + System.currentTimeMillis());
    }
    @MessageMapping("/chat/{room}")
    @SendTo("/topic/message/{room}")
    public ChatResponse chatMessage(@DestinationVariable String room,ChatRequest request){
        return ChatResponse.builder()
                .name(request.getName())
                .message(request.getMessage())
                .timestamp(String.valueOf(System.currentTimeMillis())).build();
    }
    @MessageMapping("/chat/{room}/{userId}")
//    @SendTo("/topic/message/{room}")
    public void privateChatMessage(@DestinationVariable String room,@DestinationVariable String userId, ChatRequest request){
        ChatResponse response= ChatResponse.builder()
                .name(request.getName())
                .message(request.getMessage())
                .timestamp(String.valueOf(System.currentTimeMillis())).build();
        simpMessagingTemplate.convertAndSendToUser(userId,"/queue/privateMessage/" + room,response);
    }
}
