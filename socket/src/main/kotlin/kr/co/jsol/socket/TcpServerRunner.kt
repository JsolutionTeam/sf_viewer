package kr.co.jsol.socket

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class TcpServerRunner : ApplicationRunner {

    @Autowired
    private lateinit var tcpServer: Thread

    override fun run(args: ApplicationArguments) {
        tcpServer.start()
    }
}
