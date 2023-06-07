package kr.co.jsol;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * api uri
 * /colct/api/registData.do
 *
 * api method GET
 *
 * query string format
 * key={write_api_key}&{columNm1}={value}&{columNm2}={value}&{columNm3}={value}...
 *
 * success response
 * '{"resultCode":"","resultMessage":""}'
 */

public class ExamplePost {

	// 데이터를 보낼 수집 서버 URL
    static final String API_URL = "http://iot.rda.go.kr:12345/colct/api/registData.do";

    public static void main(String[] args) throws Exception {
        // IOT 홈페이지에서 확인 가능한 쓰기 api key
        String api_key = "w4460bcc0494f41f6a3af01e54bd80c13";

        URIBuilder ub = new URIBuilder(API_URL);

        ub.addParameter("key", api_key);		// 고정 값 key, api_key
        ub.addParameter("dry_bulb_tp", "25");	// IOT 홈페이지 채널에 등록한 컬럼명, 컬럼에 해당하는 값
        ub.addParameter("wet_bulb_tp", "15");	// columNm2, value
        ub.addParameter("solrad_qy", "20");		// ...

        String url = ub.toString();

        ExampleGet ehc = new ExampleGet();
        ehc.sendData(url);
    }

    public void sendData(String requestUrl) {
        // requestUrl http://1.209.199.212:21882/colct/api/registData.do?key=apikey&columNm1=value&columNm2=value&columNm3=value...
        try{

            HttpClient client = HttpClientBuilder.create().build();
            HttpGet getRequest = new HttpGet(requestUrl);

            // request
            HttpResponse response = client.execute(getRequest);

            if (response.getStatusLine().getStatusCode() == 200) {
                // success
                ResponseHandler<String> handler = new BasicResponseHandler();
                String body = handler.handleResponse(response);

                // response body
                System.out.printf(body);
            } else {
                // response error
                System.out.printf("response is error: " + response.getStatusLine().getStatusCode());
            }

        } catch (Exception e) {
            // request error
            e.printStackTrace();
        }
    }
}

/*
    httpclient maven dependency
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.5.9</version>
    </dependency>
 */
