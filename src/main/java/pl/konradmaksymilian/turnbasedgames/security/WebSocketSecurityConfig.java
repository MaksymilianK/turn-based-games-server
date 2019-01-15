package pl.konradmaksymilian.turnbasedgames.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer  {
    
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
        		.simpMessageDestMatchers("app/topic/game-rooms/*", "app/topic/game-rooms").authenticated()
        		.simpSubscribeDestMatchers("user/queue/game-rooms/*", "user/queue/game-rooms/*/game").authenticated()
        		.simpSubscribeDestMatchers("user/queue/errors*").authenticated()
        		.anyMessage().permitAll();
    }
}