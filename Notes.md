### Class: SocketConfig implements WebSocketMessageBrokerConfigurer
> #### This class is responsible for configuring how WebSocket communication behaves in your Spring Boot app.
> 
> #### Spring Boot provides the WebSocketMessageBrokerConfigurer interface to help configure:
> 
> #### STOMP endpoints (for client connections)
> 
>#### Message broker (to route messages)
> 
>#### Destination prefixes (to distinguish between messages from client to server and server to client)

### Method 1: registerStompEndpoints(StompEndpointRegistry registry)
> #### This sets up how messages are routed between clients and the server.
>
> ### config.enableSimpleBroker("/topic", "/queue")
>
> > #### Enables a simple in-memory message broker that handles broadcasting messages to connected clients.
>
>> #### Use /topic/... when you want to broadcast messages to multiple clients (like game updates).
>
>> #### Use /queue/... for point-to-point messaging (e.g., private messages or responses to specific users).
> 
>> #### Messages from server to client go to destinations like /topic/move or /queue/join-response.
>
> ### config.enableSimpleBroker("/topic", "/queue")
>> #### This tells Spring that client-sent messages (from frontend to backend) with the /app prefix should be routed to methods annotated with @MessageMapping.