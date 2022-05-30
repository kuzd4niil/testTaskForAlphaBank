package org.kuzd4niil.testTaskForAlphaBank.clients;

import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignGifConfig {

    @Bean
    public GifJsonClient feignGifJsonContract() {
        GifJsonClient gifClient = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GifJsonClient.class, "https://api.giphy.com/v1/gifs/random");

        return gifClient;
    }
}
