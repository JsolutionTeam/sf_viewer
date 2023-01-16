package kr.co.jsol.socket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway(defaultRequestChannel = "tcpInboundChannel")
public interface TcpInboundGateway {

    @Gateway(requestChannel = "tcpInboundChannel")
    void handleTcpMessage(Message<String> message);
}
