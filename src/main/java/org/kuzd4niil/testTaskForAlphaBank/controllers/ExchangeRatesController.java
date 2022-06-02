package org.kuzd4niil.testTaskForAlphaBank.controllers;

import org.kuzd4niil.testTaskForAlphaBank.services.ExchangeRatesService;
import org.kuzd4niil.testTaskForAlphaBank.services.GifService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class ExchangeRatesController {

    private GifService gifService;
    private ExchangeRatesService exchangeRatesService;

    public ExchangeRatesController(GifService gifService, ExchangeRatesService exchangeRatesService) {
        this.gifService = gifService;
        this.exchangeRatesService = exchangeRatesService;
    }

    @GetMapping(value = "compare_rate", produces = "image/webp")
    public byte[] getGif(@RequestParam(value = "rate_currency") String rateCurrency) {
        String tag = exchangeRatesService.compareExchangeRatesTodayAndYesterday(rateCurrency) ? "rich" : "broke";

        return gifService.getImage(tag);
    }
}
