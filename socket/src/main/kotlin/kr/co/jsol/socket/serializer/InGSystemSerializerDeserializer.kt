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
        println("TCP InGSystem 역직렬화 작업 시작")

        try {
            val message = parseString(inputStream)
            log.info("message = ${message}")
            if(message.isNullOrBlank()){
                throw RuntimeException("message is null or blank")
            }
            return message
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("TCP 역직렬화 중 에러 발생 InGSystemSerializerDeserializer.deserialize() : ${e.message}")
            throw e
        }
        println("TCP InGSystem 역직렬화 작업 종료")
    }

    @Throws(IOException::class)
    private fun parseString(inputStream: InputStream): String {
        val builder = StringBuilder()
        var c: Int
        while (true) {
            c = inputStream.read()
            if (c == -1) {
                break
            }
            println("now char : ${c.toChar()}")
            builder.append(c.toChar())
        }
//        for (i in 0 until messageLength) {
//            c = inputStream.read()
//            checkClosure(c)
//            builder.append(c.toChar())
//        }
        return builder.toString()
    }
//    @Throws(IOException::class)
//    private fun parseString(inputStream: InputStream, length: Int): String {
//        val builder = StringBuilder()
//        var c: Int
//        for (i in 0 until length) {
//            c = inputStream.read()
//            checkClosure(c)
//            builder.append(c.toChar())
//        }
//        return builder.toString()
//    }

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

