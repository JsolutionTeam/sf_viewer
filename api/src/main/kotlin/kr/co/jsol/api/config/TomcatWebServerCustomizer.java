package kr.co.jsol.api.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatWebServerCustomizer
        implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Override
    public void customize(TomcatServletWebServerFactory factory) {

    }

//    /**
//     * 톰캣에 옵션 추가. []를 차단하기 때문에 오류 발생.
//     *
//     * @param factory
//     */
//    @Override
//    public void customize(TomcatServletWebServerFactory factory) {
//        factory.addConnectorCustomizers((TomcatConnectorCustomizer)
//                connector -> connector.setAttribute("relaxedQueryChars", "<>[\\]^`{|}"));
//    }
}