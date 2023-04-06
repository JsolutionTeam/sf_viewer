package kr.co.jsol.domain.common

import org.springframework.stereotype.Component
import java.util.*

@Component
class FileUtils {

    fun getUUIDFileName(originalFilename: String?): String {
        val extPosIndex: Int? = originalFilename?.lastIndexOf(".")
        val ext = originalFilename?.substring(extPosIndex?.plus(1) as Int)

        return UUID.randomUUID().toString() + "." + ext
    }
}
