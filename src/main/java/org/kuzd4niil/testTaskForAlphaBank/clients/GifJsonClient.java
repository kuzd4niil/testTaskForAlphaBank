package org.kuzd4niil.testTaskForAlphaBank.clients;

import feign.Param;
import feign.RequestLine;

public interface GifJsonClient {

    @RequestLine("GET ?api_key={api_key}&tag={tag}")
    public Object getRandomGif(
            @Param(value = "api_key") String apiKey,
            @Param(value = "tag") String tag
    );
}
