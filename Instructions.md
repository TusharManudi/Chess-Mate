## Step 1
>#### Configure application properties to connect to postgresql database 
>#### Create Models - User , Game and Moves and also their services and repos. 

### Step 2:
> #### Start working on websocket configuration 
> #### First we create an endpoint as ("/ws") or something like this in the stomp endpoint registry.
> #### Then this config will be done to send and receive messages between client and server 
> #### config.enableSimpleBroker("/topic" , "/queue"); -- for sending messages to clients
> #### config.setApplicationDestinationPrefixes("/app"); -- for client - > server messages 

### Additional Info 
> #### check and from are special keywords in postgresql so don't use it for move 
> #### example fromSquare instead of from and inCheck instead of check

### Important(WebSocket)
> #### Spring Boot provides the WebSocketMessageBrokerConfigurer interface to help configure:
> #### STOMP endpoints (for client connections)
> #### Message broker (to route messages)
> #### Destination prefixes (to distinguish between messages from client to server and server to client)