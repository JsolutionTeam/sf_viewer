package kr.co.jsol.socket.interfaces

import java.io.OutputStream

interface TcpResponseHandlerInterface {
    fun handle(outputStream: OutputStream, message: String): Unit
}
