package kr.co.jsol.socket.gateway

import org.springframework.integration.annotation.Gateway
import org.springframework.integration.annotation.MessagingGateway
import org.springframework.integration.ip.tcp.connection.TcpConnection
import org.springframework.messaging.Message
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload

@MessagingGateway(defaultRequestChannel = "tcpInboundChannel")
interface InGSystemInboundGateway {


    /**
     * 매개변수가 하나 일 때는 무조건 Payload로 인식하지만 여러 개 일때는 어떤 인자가 Payload이며 어떤 인자가 Header임을 명시해 줘야 한다.
     * 위 사항은 게이트웨이 뿐만 아니라 ServiceActivator에서도 동일하다.
     *
     * @param message TCP로 들어오는 데이터를 @Payload로 명시했다.
     */
    @Gateway(requestChannel = "tcpInboundChannel")
    fun handleTcpMessage(@Payload message: Message<String>)
}
