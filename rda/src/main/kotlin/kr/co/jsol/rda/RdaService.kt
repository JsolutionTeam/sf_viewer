package kr.co.jsol.rda

import kr.co.jsol.domain.entity.rdacolumn.RdaColumnService
import kr.co.jsol.domain.entity.site.SiteService
import kr.co.jsol.domain.entity.site.dto.request.SiteSearchCondition
import kr.co.jsol.domain.entity.site.dto.response.SummaryResponse
import kr.co.jsol.domain.entity.site.dto.response.ToSendRDAResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.time.Duration
import java.time.LocalDateTime

@Service
class RdaService(
    private val rdaColumnService: RdaColumnService,
    private val siteService: SiteService,
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

    fun send() {
        siteService.list().forEach {
            log.info("siteSeq: ${it.id}")
            sendById(it.id)
        }
    }

    fun sendById(siteSeq: Long) {
        try {

            val restTemplate: RestTemplate = RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10))
                .build()

            val uriContainer = UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .port(port)
                .path(path)


            // 각 농가 별 센서 데이터 조회
            val rdaColumns = rdaColumnService.list()
            val dataList: List<ToSendRDAResponse> = siteService.getDataToSendForRDA(
                SiteSearchCondition(
                    siteSeq = siteSeq,
                    startTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0),
                    endTime = LocalDateTime.now(),
                )
            )
            log.info("전송 데이터 수 : ${dataList.size}")

            // 데이터 전송
            dataList.forEach { data ->
                // 데이터 포맷 변경 후 데이터 전송
                val newUriContainer = uriContainer.cloneBuilder()
                rdaColumns.forEach { (property) ->
                    newUriContainer
                        .queryParam(property, property.makeData(data))
                }
                val uri = newUriContainer.build().toUri()
                log.info("uri : ${uri}")
            }

        } catch (e: Exception) {
            log.error("RDA 서버로 데이터 전송 중 에러가 발생했습니다.", e)
            e.printStackTrace()
        }
    }

    private fun String.makeData(data: ToSendRDAResponse): Double {
        return data[this]
    }
}
