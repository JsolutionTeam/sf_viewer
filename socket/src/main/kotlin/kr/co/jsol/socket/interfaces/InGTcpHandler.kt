package kr.co.jsol.socket.interfaces

import java.io.Closeable
import java.io.Flushable
import java.net.Socket

interface InGTcpHandler {
    fun makeMessageFormat(vararg messages: Any): String{
        return "*${messages.joinToString(",")}#"
    }
}
