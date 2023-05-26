package kr.co.jsol.rda

import kr.co.jsol.domain.entity.rdacolumn.RdaColumnService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.time.Duration

class RdaService(
    private val rdaColumnService: RdaColumnService,
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

    fun sendSensorData() {
        try {

            val restTemplate: RestTemplate = RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10))
                .build()

            val uri = UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .port(port)
                .path(path)

            rdaColumnService.list().forEach {

                uri.queryParam()
            }

        } catch (Exception e) {
            log.error("Exception : " + e + "========== 익셥센 발생!! ==========");
        }
    }
}
