package kr.co.jsol.socket.serializer

import org.apache.commons.lang3.StringUtils
import org.apache.commons.logging.LogFactory
import org.springframework.core.serializer.Deserializer
import org.springframework.core.serializer.Serializer
import org.springframework.stereotype.Component
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

@Component
class InGSystemSerializerDeserializer : Serializer<String>, Deserializer<String> {
    protected val log = LogFactory.getLog(this.javaClass)

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
            val message = parseString(inputStream)

            if(message.isNullOrBlank()) {
                throw IOException("payload is null")
            }

            log.info("message = ${message}")
            return message
        }catch (e: IOException){
            log.error("TCP 역직렬화 중 IOException 발생 message : ${e.message}")
            if(e.message != "payload is null") {
                e.printStackTrace()
                throw e
            }
            throw IOException("payload is null")
        }
        catch (e: Exception) {
            e.printStackTrace()
            log.error("TCP 역직렬화 중 에러 발생 InGSystemSerializerDeserializer.deserialize() : ${e.message}")
            throw e
        }
        log.info("TCP InGSystem 역직렬화 작업 종료")
    }

    private fun isEOF(value: Int): Boolean {
        return value == -1
    }

    @Throws(IOException::class)
    private fun parseString(inputStream: InputStream): String {
        val builder = StringBuilder()
        var c: Int


        while (true) {
            c = inputStream.read()


            // -1 은 EOF임. 항상 마지막에 EOF을 나타내고 있음. 이 값이 읽히기 전까지는 데이터 input stream에 값이 남아있게 되므로
            // 10 12 13과 같은 값은 continue로 계속 읽도록 처리하고 -1만 break로 종료 함.
            if(isEOF(c)){
                break
            }

            // 10 = LF 13 = CR 인데, 이 프로그램에서 10 13이 넘어오면 새로운 데이터가 들어올 예정이므로 종료한다.
            if(c == 10 || c == 12 || c == 13){
                continue
            }

            builder.append(c.toChar())

        }
            log.info("parsing result : ${builder.toString()}")
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

