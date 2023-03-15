package kr.co.jsol.socket.interfaces

interface InGTcpHandler {
    fun makeMessageFormat(vararg messages: Any): String {
        return "*${messages.joinToString(",")}#"
    }
}
