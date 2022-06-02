package org.kuzd4niil.testTaskForAlphaBank.clients;

import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Value("${giphy.httpEndpoint}")
    private String giphyHttpEndpoint;
    @Value("${giphy.mediaEndpoint}")
    private String giphyMediaEndpoint;
    @Value("${openexchangerates.httpEndpoint}")
    private String openexchangeratesHttpEndpoint;

    @Bean
    public GifJsonClient feignGifJsonContract() {
        GifJsonClient gifClient = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GifJsonClient.class, giphyHttpEndpoint);

        return gifClient;
    }

    @Bean
    public WebpClient feignWebpContract() {
        WebpClient webpClient = Feign.builder()
                .target(WebpClient.class, giphyMediaEndpoint);

        return webpClient;
    }

    @Bean
    public ExchangeRatesClient feignExchangeRatesContract() {
        ExchangeRatesClient exchangeRatesClient = Feign.builder()
                .decoder(new GsonDecoder())
                .target(ExchangeRatesClient.class, openexchangeratesHttpEndpoint);

        return exchangeRatesClient;
    }
}
