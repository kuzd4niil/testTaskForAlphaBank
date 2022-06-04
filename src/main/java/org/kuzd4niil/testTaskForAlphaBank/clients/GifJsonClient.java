package org.kuzd4niil.testTaskForAlphaBank.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${giphy.httpEndpointName}", url = "${giphy.httpEndpoint}")
public interface GifJsonClient {

    @GetMapping
    public Object getRandomGif(
            @RequestParam(value = "api_key") String apiKey,
            @RequestParam(value = "tag") String tag
    );
}
