package com.thucjava.shopapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${app.cors.allowed-origin}")
    private String crossOrigin;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").setAllowedOrigins(crossOrigin).withSockJS();
        // thiết lập kết nối, cho phép sử dụng giao thức stomp
        // end point để client sẽ kết nối WebSocket Server tại http://localhost:8080/chat
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // endponit client gửi tin nhắn đến server
        registry.setApplicationDestinationPrefixes("/app"); // @MessageMapping
        // kích hoạt một simple message broker để gửi tin nhắn từ server về client
        registry.enableSimpleBroker("/topic"); // return /topic/messages
    }
}
