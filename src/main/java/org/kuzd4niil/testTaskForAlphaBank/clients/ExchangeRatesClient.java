package org.kuzd4niil.testTaskForAlphaBank.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;

@FeignClient(name = "${openExchangeRates.httpEndpointName}", url = "${openExchangeRates.httpEndpoint}")
public interface ExchangeRatesClient {

    @GetMapping("historical/{date}.json")
    public Object getSpecificDateExchangeRates(
            @PathVariable(value = "date") String date,
            @RequestParam(value = "app_id") String appId,
            @RequestParam(value = "base_currency") String baseCurrency
    );

    @GetMapping("latest.json")
    public Object getTodayExchangeRates(
            @RequestParam(value = "app_id") String appId,
            @RequestParam(value = "base_currency") String baseCurrency
    );

    @GetMapping("currencies.json")
    public LinkedHashMap<String, String> getCurrencies();
}
