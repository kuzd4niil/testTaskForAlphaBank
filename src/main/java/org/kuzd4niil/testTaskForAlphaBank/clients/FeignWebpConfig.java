package org.kuzd4niil.testTaskForAlphaBank.clients;

import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignWebpConfig {

    @Bean
    public WebpClient feignWebpContract() {
        WebpClient webpClient = Feign.builder()
                .target(WebpClient.class, "https://i.giphy.com/media");

        return webpClient;
    }
}
