package kr.co.jsol.socket

import kr.co.jsol.domain.entity.ingsystem.InGSystemService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory
import org.springframework.messaging.MessageChannel


@Configuration
class TcpServerConfig {

    private val log = LoggerFactory.getLogger(TcpServerConfig::class.java)

    @Value("\${tcp.server.port}")
    private val port = 0

    @Autowired
    private val serialDeserializer: InGSystemSerializerDeserializer? = null

    @Bean
    fun serverFactory(): TcpNetServerConnectionFactory {
        if (serialDeserializer == null) {
            throw RuntimeException("serialDeserializer is null")
        }
        log.info("TCP 서버 실행, port : ${port}")
        val factory = TcpNetServerConnectionFactory(port)
        factory.serializer = serialDeserializer
        factory.deserializer = serialDeserializer
        return factory
    }

    @Bean
    fun inbound(): TcpReceivingChannelAdapter {
        log.info("inbound 처리")
        val adapter = TcpReceivingChannelAdapter()
        adapter.setConnectionFactory(serverFactory())
        adapter.outputChannel = tcpInboundChannel()

        adapter.retryInterval = 1000
        adapter.setSendTimeout(5000)

        return adapter
    }

    @Bean
    fun tcpInboundChannel(): MessageChannel {
        log.info("tcpInboundChannel 처리")
        return DirectChannel()
    }

    @Bean
    fun tcpServerHandler(inGSystemService: InGSystemService?): TcpInGSystemService {
        log.info("tcpServerHandler 처리")
        return TcpInGSystemService(inGSystemService!!)
    }

    @Bean
    fun inboundFlow(inbound: TcpReceivingChannelAdapter?, tcpServerHandler: TcpInGSystemService?): IntegrationFlow {
        log.info("inboundFlow 처리")
        return IntegrationFlows
            .from(inbound)
            .handle(tcpServerHandler)
            .get()
    }
}
