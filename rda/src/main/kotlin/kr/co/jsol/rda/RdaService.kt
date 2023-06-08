package kr.co.jsol.rda

import kr.co.jsol.domain.entity.micro.MicroRepository
import kr.co.jsol.domain.entity.rdacolumn.RdaColumnService
import kr.co.jsol.domain.entity.rdacolumn.dto.response.RdaColumnResponse
import kr.co.jsol.domain.entity.sensor.SensorRepository
import kr.co.jsol.domain.entity.site.SiteService
import kr.co.jsol.rda.dto.ToSendRDAResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

@Service
class RdaService(
    private val rdaQuery: RdaQuery,
    private val rdaColumnService: RdaColumnService,
    private val siteService: SiteService,

    private val microRepository: MicroRepository,
    private val sensorRepository: SensorRepository,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Value("\${rda.scheme}")
    private lateinit var scheme: String

    @Value("\${rda.host}")
    private lateinit var host: String

    @Value("\${rda.port}")
    private lateinit var port: String

    @Value("\${rda.path}")
    private lateinit var path: String

    // RDA 서버로 데이터 전송을 위한 http client 생성
    private val client: HttpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.of(10, java.time.temporal.ChronoUnit.MINUTES))
        .build()

    private var uriContainer: UriComponentsBuilder = UriComponentsBuilder.newInstance()
        get() = UriComponentsBuilder.newInstance()
            .scheme(scheme)
            .host(host)
            .port(port)
            .path(path)

    fun send(rdaColumns: List<RdaColumnResponse>): Long {
        var sendCount = 0L
        siteService.list().forEach { site ->
            val siteSeq = site.id
            sendCount += sendById(siteSeq, rdaColumns)
        }
        return sendCount
    }

    fun sendById(siteSeq: Long, rdaColumns: List<RdaColumnResponse>): Long {
        var sendCount = 0L

        // site에 apiKey가 없다면 아래 작업은 패스한다.
        if (siteService.isAbleToSendRda(siteSeq).not()) return 0L

        // 센서 데이터 전송
        sendCount += findSendIsFalsyAndSendBySiteSeq(rdaColumns, siteSeq, ERdaType.SENSOR)
        // 마이크로 데이터 전송
        sendCount += findSendIsFalsyAndSendBySiteSeq(rdaColumns, siteSeq, ERdaType.MICRO)
        return sendCount
    }

    private fun findSendIsFalsyAndSendBySiteSeq(rdaColumns: List<RdaColumnResponse>, siteSeq: Long, type: ERdaType): Long {
        // 첫 번째 요소는 성공 개수, 두 번째 요소는 실패 개수
        var sendCount = 0L
        try {
            // 각 농가 별 센서 데이터 조회
            val toSendData: List<ToSendRDAResponse> = getDataToSendForRDA(siteSeq, type)

            // 성공한 id를 담을 리스트, 전송 후에 isSend를 true로 만들기 위해 사용
            val ids = mutableListOf<Long>()
            for (data in toSendData) {
                // 하나라도 전송 실패 시 다음에올 다른 요청도 전부 실패하기 때문에 요청을 중지한다.
                val id = sendRequestAndReturnId(data, rdaColumns) ?: break
                ids.add(id)
            }

            // 전송 성공한 데이터는 전송 완료 처리
            if (ids.isNotEmpty()) {
                updateSendStatus(ids, type)
                sendCount = ids.size.toLong()
            }
        } catch (e: Exception) {
            log.error("RDA 서버로 데이터 처리 중 에러가 발생했습니다.", e)
            e.printStackTrace()
        }
        return sendCount
    }

    private fun updateSendStatus(ids: List<Long>, type: ERdaType) {
        log.info("ids : ${ids.joinToString(",")}")
        when (type) {
            ERdaType.MICRO -> microRepository.updateSendStatus(ids)
            ERdaType.SENSOR -> sensorRepository.updateSendStatus(ids)
            else -> throw IllegalArgumentException("지원하지 않는 타입입니다.")
        }
    }

    private fun sendRequestAndReturnId(data: ToSendRDAResponse, rdaColumns: List<RdaColumnResponse>): Long? {
        return try {
            // 데이터 포맷 변경 후 데이터 전송
            val newUriContainer = uriContainer.cloneBuilder()

            rdaColumns.forEach { rdaColumn ->
                val target = rdaColumn.target
                val property = rdaColumn.property
                val value = property.makeData(data)
                newUriContainer.queryParam(target, value)
            }

            val uri: URI = newUriContainer.build().toUri()
            log.info("uri : $uri")
            val responseBody = requestDataToUri(client, uri)
            log.info("API 요청 결과 : $responseBody")

            // resultCode가 빈 문자열로 오는 경우 성공.
            /*
                요청에 apiKey가 없는 경우 : E_EMPTY_KEY
                불가능한 apiKey가 온 경우 : E_INVALID_KEY
             */
            if (responseBody.contains("\"resultCode\":\"\"")) {
                data.id
            } else {
                null
            }
        } catch (e: Exception) {
            log.error("RDA 서버로 데이터 전송 중 에러가 발생했습니다.", e)
            e.printStackTrace()
            null
        }
    }

    private fun requestDataToUri(client: HttpClient, uri: URI): String {
        val request = HttpRequest.newBuilder()
            .GET()
            .uri(uri)
            .build()

        // 응답 데이터 반환
        return client.send(request, HttpResponse.BodyHandlers.ofString())
            .body()
    }

    private fun String.makeData(data: ToSendRDAResponse): Any {
        return data[this]
    }

    fun getDataToSendForRDA(siteSeq: Long, type: ERdaType): List<ToSendRDAResponse> {
        return rdaQuery.getDataToSendForRDA(siteSeq, type)
    }
}
