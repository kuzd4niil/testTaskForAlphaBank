package org.kuzd4niil.testTaskForAlphaBank.clients;

import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public GifJsonClient feignGifJsonContract() {
        GifJsonClient gifClient = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GifJsonClient.class, "https://api.giphy.com/v1/gifs/random");

        return gifClient;
    }

    @Bean
    public WebpClient feignWebpContract() {
        WebpClient webpClient = Feign.builder()
                .target(WebpClient.class, "https://i.giphy.com/media");

        return webpClient;
    }

    @Bean
    public ExchangeRatesClient feignExchangeRatesContract() {
        ExchangeRatesClient exchangeRatesClient = Feign.builder()
                .decoder(new GsonDecoder())
                .target(ExchangeRatesClient.class, "https://openexchangerates.org/api");

        return exchangeRatesClient;
    }
}
