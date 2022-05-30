package org.kuzd4niil.testTaskForAlphaBank.controller;

import org.kuzd4niil.testTaskForAlphaBank.services.GifService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class ExchangeRatesController {

    private GifService gifService;

    public ExchangeRatesController(GifService gifService) {
        this.gifService = gifService;
    }

    @GetMapping(value = "compare_rate", produces = "image/webp")
    public byte[] getGif() {

        return gifService.getImage("rich");
    }
}
