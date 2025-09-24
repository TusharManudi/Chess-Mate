package com.chess.chess.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class SocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtChannelInterceptor jwtChannelInterceptor;

    public SocketConfig(JwtChannelInterceptor jwtChannelInterceptor) {
        this.jwtChannelInterceptor = jwtChannelInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/games")
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("http://localhost:3000", "http://localhost:5173")
                .withSockJS(); // Enable SockJS fallback
        
        // Also register without SockJS for native WebSocket
        registry.addEndpoint("/games")
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("http://localhost:3000", "http://localhost:5173");
        
        //client will connect on this endpoint. ws://localhost8080/games
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        config.enableSimpleBroker("/topic" , "/queue"); /* for sending messages to clients
         "/topic" for if you want to broadcast a message to all users subscribed
         "/queue" for private messages || messages to a single user */

        config.setApplicationDestinationPrefixes("/app");
        // for client - > server messages
        /* sent from frontend to backend with /app prefix should be routed to methods annotated with
         @MessageMapping */

        // Enables spring to route user specific messages to correct users .
        config.setUserDestinationPrefix("/user");

    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtChannelInterceptor);
    }

}
