package kr.co.jsol;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * api uri
 * /api/{read_api_key}/{date}/{hour}
 * /api/w4460bcc0494f41f6a3af01e54bd80c13/20191121/13

 * success response
 * {
 *    "datas":
 *        [
 *            {"dt":"2019-11-21 13:41:55","dry_bulb_tp":"25","wet_bulb_tp":"15","solrad_qy":"20"},
 *            {"dt":"2019-11-21 13:42:13","dry_bulb_tp":"25","wet_bulb_tp":"15","solrad_qy":"20"},
 *            ...
 *        ],
 *    "resultCode":"",
 *    "resultMessage":""
 * }
 */

public class ExampleGet {

	//데이터를 읽을 서버 URL
    static final String API_URL = "http://iot.rda.go.kr/api";

    public static void main(String[] args) throws Exception {
        // IOT 홈페이지 에서 확인 가능한 읽기 api key
        String api_key = "re94cc119a35b4cfb99f99316fe26af95";
		// 조회 하고자 하는 날짜
        String date = "20191121";
		// 조회 하고자 하는 시간
        String hour = "13";

        // api_url/{read_api_key}/{date}/{hour}
        String url = String.format("%s/%s/%s/%s", API_URL, api_key, date, hour);

        ExampleGet ehc = new ExampleGet();
        ehc.sendData(url);
    }

    /**
     * request api
     * @param requestUrl
     */
    public void sendData(String requestUrl) {
       try{

            HttpClient client = HttpClientBuilder.create().build();
            HttpGet getRequest = new HttpGet(requestUrl);

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
