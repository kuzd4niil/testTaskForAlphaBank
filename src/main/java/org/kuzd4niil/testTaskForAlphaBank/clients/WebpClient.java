package org.kuzd4niil.testTaskForAlphaBank.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${giphy.mediaEndpointName}", url = "${giphy.mediaEndpoint}")
public interface WebpClient {

    @GetMapping("{webp_id}/giphy.webp")
    public byte[] getWebp(
            @PathVariable(value = "webp_id") String webpId
    );
}
