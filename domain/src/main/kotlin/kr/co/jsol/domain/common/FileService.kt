package kr.co.jsol.domain.common

import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.util.*

@Service
class FileService(
    private val fileUtils: FileUtils,
) {
    private val log = getLogger(this.javaClass)

    @Value("\${file.uploadDir:}")
    private lateinit var UPLOAD_DIR: String // 파일을 저장할 디렉토리 경로

    @Value("\${file.loadPath:}")
    private lateinit var LOAD_PATH: String // 파일을 불러올 경로

    /**
     * 파일 업로드
     * @param file 업로드할 파일
     * @param subDir 업로드할 서브 디렉토리 /로 시작하지 않으며 /로 끝내야 한다.<br>
     * ex) sensorDevice/image/
     */
    @Throws(IOException::class)
    fun uploadFile(file: MultipartFile, subDir: String = ""): String {
        if(UPLOAD_DIR.isBlank()) {
            return ""
        }
        val uploadDir = (UPLOAD_DIR + subDir)
            .replace("//", "/")
            .replace("\\", "/") // 업로드될 디렉토리

        // 원본파일이름, 저장될 파일이름
        var originalFilename: String? = getOriginalFileName(file)

        val fileName = fileUtils.getUUIDFileName(originalFilename)

        // 업로드 경로를 확인하고 없으면 만듦
        checkAndMakeDir(uploadDir)

        val saveFile = File(uploadDir + fileName) // 저장될 파일 경로
        file.transferTo(saveFile) // 파일 저장

        val fileSeparator = File.separator
        var imgPath = "$LOAD_PATH/$subDir"
        if (isNotEndsWithSlash(imgPath)) {
            imgPath += fileSeparator
        }

        return imgPath + fileName
    }

    private fun isNotEndsWithSlash(dir: String): Boolean {
        return !dir.endsWith("/")
    }

    private fun getOriginalFileName(file: MultipartFile): String? {
        var originalFilename: String? = file.originalFilename
        if (originalFilename?.contains("\\") == true) {
            originalFilename = originalFilename.substring(originalFilename.lastIndexOf("\\") + 1)
        }
        return originalFilename
    }

    private fun checkAndMakeDir(dir: String) {
        var dir = dir.replace("\\", "/")
        log.info("경로 탐색 dir : $dir")
        var lastPath = ""

        dir.split("/").filter {
            !it.isNullOrBlank()
        }.forEach {
            // 현재 비교할 경로를 지정
            lastPath += "/$it"

            // 현재 경로로 생성
            val file = File(lastPath)
            // 현재 경로로 파일이 존재하지 않다면
            if (!file.exists()) {
                // 폴더를 생성한다
                file.mkdirs()
            }
        }
    }

    fun deleteFile(imgPath: String) {
        try {
            if(UPLOAD_DIR.isBlank()) {
                return
            }
            // imgPath = /api/media/sensorDevice/3/a72fbcf7-12d3-4466-9f3b-cc89ca7e16bf.JPG
            val fileUri = imgPath.replace(LOAD_PATH, "") // /api/media -> ""
            // fileUri = /sensorDevice/3/a72fbcf7-12d3-4466-9f3b-cc89ca7e16bf.JPG
            val filePath = (UPLOAD_DIR + fileUri).replace("//", "/")
            // fileUri = C:/jsolution/files/sensorDevice/3/a72fbcf7-12d3-4466-9f3b-cc89ca7e16bf.JPG

            // "/" 로 split 했을 때 제일 뒤에서 두 개의 요소를 판단하여 삭제하면 폴더까지 삭제할 수 있다.
            // ex) /sensorDevice/3/a72fbcf7-12d3-4466-9f3b-cc89ca7e16bf.JPG
            // -> UPLOAD_DIR + /sensorDevice/3
            // -> UPLOAD_DIR + /sensorDevice/3/a72fbcf7-12d3-4466-9f3b-cc89ca7e16bf.JPG

            val folderPath = filePath.split("/").dropLast(1).joinToString("/")
            // folderPath = C:/jsolution/files/sensorDevice/3
            val file = File(folderPath) // 경로로 삭제할 폴더 로드
            log.info("folderPath : $folderPath")
            log.info("folder.exists() : ${file.exists()}")
            if (file.isDirectory && file.exists()) { // 폴더가 존재한다면 삭제
                log.info("폴더 삭제 로직 진행")
                // 하위 파일들 삭제
                file.listFiles()?.forEach {
                    log.info("하위 파일 삭제 ${it.name}")
                    if (it.exists()) it.delete()
                }

                log.info("폴더 삭제")
                file.delete()
            } else if (file.isFile && file.exists()) {
                log.info("단일 파일 삭제")
                file.delete()
            }
        } catch (_: Exception) {
            log.error("$imgPath 파일 삭제 실패")
        }
    }
}
