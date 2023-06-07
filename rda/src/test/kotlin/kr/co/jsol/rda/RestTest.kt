package kr.co.jsol.rda

import org.junit.jupiter.api.Test
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class RestTest {

    @Test
    fun httpTest() {
        val uri =
            URI(
                "http://iot.rda.go.kr:12345/colct" +
                    "/api/registData.do?key=wecef66df368d40a0bf3c5d4f2b9d22ee" +
                    "&frmhs_nm=센서_개발&examin_datetm=2023-05-29T14:30:02&tp_extrl=25.234" +
                    "&partn_hd_extrl=46.4&wd=0.0&arvlty=0.0&solrad_qy=881.9" +
                    "&rain_qy=0.0&soil_moisture_t=0.0&soil_moisture_c=0.0&tp_soil_1=0.0"
            )
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .GET()
            .uri(uri)
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        println(response)
    }
}
