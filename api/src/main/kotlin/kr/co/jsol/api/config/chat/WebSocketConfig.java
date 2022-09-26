package kr.co.jsol.api.config.chat;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final WebSocketInterceptor webSocketInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { // stomp 사용시..
        // 연결 URL : ws://ip:port/ws-stomp/websocket
        // http = ws , https = wss  port/endPoint/websocket
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("http://localhost:3000", "10.0.2.16:8080", "http://localhost:3001", "http://localhost:5005",
                        "http://localhost:8088", "http://localhost:80", "https://localhost:3000",
                        "https://localhost:3001", "https://localhost:5005", "https://localhost:8088", "https://localhost:80") // react 포트가 3000이라서 3000으로 뒀었음. 모든 포트 허용 : ** 은 allowCredentials가 true라서 안된다고 함. 뭐지?
                .withSockJS();
        // withSockJs 사용시의 장점
        // wsebsocket 형태로 ㅂ연결이 불가능한 경우 http를 사용해서 연결이 지속되도록 만든다는 뜻.
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketInterceptor);
    }
}

//@Controller
//@Configurable
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/topic");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/start").setAllowedOrigins("http://localhost:3000").withSockJS();
//    }
//}
//
