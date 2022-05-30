package org.kuzd4niil.testTaskForAlphaBank.clients;

import feign.Param;
import feign.RequestLine;

public interface WebpClient {

    @RequestLine("GET /{webp_id}/giphy.webp")
    public byte[] getWebp(
            @Param(value = "webp_id") String webpId
    );
}
