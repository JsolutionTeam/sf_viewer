package kr.co.jsol.socket.interfaces

import java.io.OutputStream
import java.net.Socket

interface TcpResponseHandlerInterface {
    fun handle(outputStream: OutputStream, message: String): Unit
}
