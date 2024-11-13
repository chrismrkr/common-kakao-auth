package common.loginapiserver.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigure implements WebMvcConfigurer {
    @Value("${oauth2.url.allow-origin}")
    private String allowedOrigin;
    @Override
    public void addCorsMappings(CorsRegistry registry) { // CORS config
        registry.addMapping("/**") // 허용되는 url 패턴
                .allowedOrigins(allowedOrigin)
                .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true); // 자격증명 허용
                ; // 허용 시간
    }
}
