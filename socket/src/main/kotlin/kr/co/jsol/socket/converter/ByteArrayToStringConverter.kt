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
 */
package kr.co.jsol.socket.converter

import org.springframework.core.convert.converter.Converter
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

/**
 * 스프링 인티그레이션 TCP-Client-Server 샘플 참고
 */
class ByteArrayToStringConverter : Converter<ByteArray?, String> {
    /**
     * @return the charSet
     */
    /**
     * @param charSet the charSet to set
     */
    private var charSet: Charset = Charset.forName("UTF-8")
    override fun convert(bytes: ByteArray): String? {
        return try {
            String(bytes, charSet)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            String(bytes)
        }
    }

}
