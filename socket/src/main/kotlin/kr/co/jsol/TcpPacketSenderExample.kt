package kr.co.jsol

import java.net.Socket

object TcpPacketSenderExample {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        var socket: Socket? = null //new Socket의 참조값을 담을 지역변수. try 블럭과 finally 블럭에서
        val serverIp = "127.0.0.1"
        val serverPort = 18081
        val message = "*133,-1#"


        try{
            val socket = Socket(serverIp, serverPort) //new Socket("server 주소", port 번호). new하는 시점에 동작함
            //연결에 성공하면 서버에 있는 socket 객체와 일대일로 연결된 socket 객체의 참조값이 반환된다.
            println("socket 연결 성공") //serverSocket과 연결에 성공시 출력될 문자열

            val out = socket.getOutputStream()
            val messageByteArray = message.toByteArray()

            messageByteArray.forEach { byte ->
                println("byte = ${byte}")
            }

            out.write(messageByteArray)
            out.flush()
            socket.close()
        } catch (e: Exception) {    //new Socket으로 인해 발생한 exception handling
            e.printStackTrace()
        } finally {
            try {
                socket?.close() //socket 사용 후 닫아주기
            } catch (e: Exception) {    //socket.close();으로 인해 발생한 exception handling
                e.printStackTrace()
            }
        }


        return
    }
}
