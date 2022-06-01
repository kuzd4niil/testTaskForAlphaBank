package org.kuzd4niil.testTaskForAlphaBank.clients;

import feign.Param;
import feign.RequestLine;

public interface ExchangeRatesClient {

    @RequestLine("GET /historical/{date}.json?app_id={app_id}&base={base_currency}")
    public Object getSpecificDateExchangeRates(
            @Param(value = "date") String date,
            @Param(value = "app_id") String appId,
            @Param(value = "base_currency") String baseCurrency
    );

    @RequestLine("GET /latest.json?app_id={app_id}&base={base_currency}")
    public Object getTodayExchangeRates(
            @Param(value = "app_id") String appId,
            @Param(value = "base_currency") String baseCurrency
    );
}
