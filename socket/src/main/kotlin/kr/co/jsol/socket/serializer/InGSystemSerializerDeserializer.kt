package kr.co.jsol.socket.serializer

import kr.co.jsol.domain.entity.ingsystem.InGSystem
import org.apache.commons.lang3.StringUtils
import org.apache.commons.logging.LogFactory
import org.springframework.core.serializer.Deserializer
import org.springframework.core.serializer.Serializer
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ /**
 * This class is used to demonstrate how you can create a custom serializer/deserializer
 * to convert a TCP stream into custom objects which your domain-specific code can use.
 *
 * Since this is custom, it will have to have its own predefined assumptions for dealing
 * with the stream. In other words, there will have to be some indication within the
 * contents of the stream where the beginning/end is and how to extract the contents
 * into something meaningful (like an object). An example would be a fixed-file formatted
 * stream with the length encoded in some well known part of the stream (for example, the
 * first 8 bytes of the stream?).
 *
 * This custom serializer/deserializer assumes the first 3 bytes of the stream will be
 * considered an Order Number, the next 10 bytes will be the Sender's Name, the next 6 bytes
 * represents an left-zero-padded integer that specifies how long the rest of the message
 * content is. After that message content is parsed from the stream, the stream is assumed
 * to not have anything after it. In your code you could have delimiters to mark the end
 * of the stream, or could agree with the client that a valid stream is only n characters,
 * etc. Either way, since its custom, the client and server must have some predefined
 * assumptions in place for the communication to take place.
 *
 *
 * @author Christian Posta
 * @author Gunnar Hillert
 */
class InGSystemSerializerDeserializer : Serializer<InGSystem>, Deserializer<InGSystem> {
    protected val logger = LogFactory.getLog(this.javaClass)

    private val charset = Charsets.UTF_8

    /**
     * Convert a InGSystem object into a byte-stream
     *
     * @param object
     * @param outputStream
     * @throws IOException
     */
    @Throws(IOException::class)
    override fun serialize(`object`: InGSystem, outputStream: OutputStream) {
        val rateOfOpening = `object`.rateOfOpening.toString().toByteArray(charset)
        outputStream.write(rateOfOpening)

        val openSignal: ByteArray = `object`.openSignal.toString().toByteArray(charset)
        outputStream.write(openSignal)

//        val lengthPadded = pad(6, `object`.getMessage().length())
//        val length = lengthPadded.toByteArray()
//        outputStream.write(length)
//        outputStream.write(`object`.getMessage().getBytes())
        outputStream.flush()
    }

    private fun pad(desiredLength: Int, length: Int): String {
        return StringUtils.leftPad(length.toString(), desiredLength, '0')
    }

    /**
     * Convert a raw byte stream into a InGSystem
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    override fun deserialize(inputStream: InputStream): InGSystem {
        val rateOfOpening = parseRateOfOpening(inputStream)
        val senderName = parseOpenSignal(inputStream)
        //        val message = parseMessage(inputStream)
//        inGSystem.setMessage(message)
        return InGSystem(
            rateOfOpening = rateOfOpening.toDouble(),
            openSignal = senderName.toInt(),
        )
    }

    @Throws(IOException::class)
    private fun parseMessage(inputStream: InputStream): String {
        val lengthString =
            parseString(inputStream, OPEN_SIGNAL_LENGTH)
        val lengthOfMessage = Integer.valueOf(lengthString)
        return parseString(inputStream, lengthOfMessage)
    }

    @Throws(IOException::class)
    private fun parseString(inputStream: InputStream, length: Int): String {
        val builder = StringBuilder()
        var c: Int
        for (i in 0 until length) {
            c = inputStream.read()
            checkClosure(c)
            builder.append(c.toChar())
        }
        return builder.toString()
    }

    @Throws(IOException::class)
    private fun parseOpenSignal(inputStream: InputStream): String {
        return parseString(inputStream, OPEN_SIGNAL_LENGTH)
    }

    @Throws(IOException::class)
    private fun parseRateOfOpening(inputStream: InputStream): Int {
        val value = parseString(inputStream, RATE_OF_OPENING)
        return Integer.valueOf(value)
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
            logger.debug("Socket closed during message assembly")
            throw IOException("Socket closed during message assembly")
        }
    }

    companion object {
        private const val RATE_OF_OPENING = 4
        private const val OPEN_SIGNAL_LENGTH = 4
    }
}
