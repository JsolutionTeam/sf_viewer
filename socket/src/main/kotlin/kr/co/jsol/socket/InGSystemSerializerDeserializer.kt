package kr.co.jsol.socket

import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.serializer.Deserializer
import org.springframework.core.serializer.Serializer
import org.springframework.stereotype.Component
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.SocketException

@Component
class InGSystemSerializerDeserializer : Serializer<String>, Deserializer<String> {
    protected val log: Logger = LoggerFactory.getLogger(this.javaClass)

    private val charset = Charsets.UTF_8

    override fun serialize(`object`: String, outputStream: OutputStream) {
        println("TCP InGSystem 직렬화 작업 시작")

        try {
            outputStream.write(`object`.toByteArray(charset))
//            val rateOfOpening = `object`.rateOfOpening.toString().toByteArray(charset)
//            outputStream.write(rateOfOpening)
//
//            val openSignal: ByteArray = `object`.openSignal.toString().toByteArray(charset)
//            outputStream.write(openSignal)

//        val lengthPadded = pad(6, `object`.getMessage().length())
//        val length = lengthPadded.toByteArray()
//        outputStream.write(length)
//        outputStream.write(`object`.getMessage().getBytes())
            outputStream.flush()
        } catch (e: Exception) {
            error("TCP 직렬화 중 에러 발생 InGSystemSerializerDeserializer.serialize() : ${e.message}")
            throw e
        }
        println("TCP InGSystem 직렬화 작업 종료")
    }

    private fun pad(desiredLength: Int, length: Int): String {
        return StringUtils.leftPad(length.toString(), desiredLength, '0')
    }

    override fun deserialize(inputStream: InputStream): String {
        log.info("TCP InGSystem 역직렬화 작업 시작")
        try {
//            val writer: BufferedWriter = BufferedWriter(System.out.writer(), 100)
//            inputStream.bufferedReader().copyTo(writer)
//            log.info("writer info : ${writer}")
//            writer.flush()
            val message = inputStream.readAllBytes().toString(charset)
//            val message = writer.
//            val message = parseString(inputStream)
            log.info("message = $message")

//            log.info("is closed : ${inputStream.}")

            if (message.isNullOrBlank()) {
                throw IOException("payload is null")
            }

            return message
        } catch (e: SocketException) {
            log.error(e.stackTraceToString())
            log.error("TCP 역직렬화 중 Socket Exception 발생 : ${e.message}")
            throw e
        } catch (e: IOException) {
            log.error("TCP 역직렬화 중 IOException 발생 message : ${e.message}")
            if (e.message != "payload is null") {
                log.error(e.stackTraceToString())
                throw e
            }
            throw IOException("payload is null")
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("TCP 역직렬화 중 에러 발생 InGSystemSerializerDeserializer.deserialize() : ${e.message}")
            throw e
        } finally {
//            log.info("TCP InGSystem 역직렬화 작업 종료")
//            inputStream.close()
        }
    }

    private fun isEOF(value: Int): Boolean {
        return value == -1
    }

    private fun parseString(inputStream: InputStream): String {
        val builder = StringBuilder()
        var c: Int

        while (true) {
            // READ 시 상대방이 소켓 연결을 끊을 시 connection reset 에러 발생.
            // write 하지 않으므로 connection reset by peer 에러 발생은 하지 않는다.
            c = inputStream.read()

            // -1 은 EOF임. 항상 마지막에 EOF을 나타내고 있음. 이 값이 읽히기 전까지는 데이터 input stream에 값이 남아있게 되므로
            // 10 12 13과 같은 값은 continue로 계속 읽도록 처리하고 -1만 break로 종료 함.
            if (isEOF(c)) {
                break
            }

            // 10 = LF 13 = CR 인데, 이 프로그램에서 10 13이 넘어오면 새로운 데이터가 들어올 예정이므로 종료한다.
            if (c == 10 || c == 12 || c == 13) {
                continue
            }

            builder.append(c.toChar())
        }
        return builder.toString()
    }

    /**
     * Check whether the byte passed in is the "closed socket" byte
     * Note, I put this in here just as an example, but you could just extend the
     * [org.springframework.integration.ip.tcp.serializer.AbstractByteArraySerializer] class
     * which has this method
     *
     * @param bite
     * @throws IOException
     */
    @Throws(IOException::class)
    protected fun checkClosure(bite: Int) {
        if (bite < 0) {
            log.debug("Socket closed during message assembly")
            throw IOException("Socket closed during message assembly")
        }
    }
}
