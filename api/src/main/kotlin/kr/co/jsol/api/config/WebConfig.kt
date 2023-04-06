package kr.co.jsol.api.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    @Value("\${param.media}")
    private val DOWNLOAD_PATH: String? = null
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE")
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/media/**") // media로 오는 데이터를
            // DOWNLOAD_PATH 에서 찾는다.
            // media/abc.mp4  -> DOWNLOAD_PATH/abc.mp4
            // .addResourceLocations("file:///C:/Users/Administrator/Desktop/view/TOMCAT9/webapps/media/")
            .addResourceLocations(DOWNLOAD_PATH) // .addResourceLocations("file:////Users/jo/dev/jsol/RE-KNUH/video/")
            .setCachePeriod(20)
        // registry.addResourceHandler("/plugin/**")
        //   .addResourceLocations("classpath:/plugin/")
        //   .setCachePeriod(20);
    }
}
