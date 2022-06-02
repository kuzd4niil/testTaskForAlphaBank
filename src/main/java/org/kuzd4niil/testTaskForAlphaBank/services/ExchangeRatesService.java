package org.kuzd4niil.testTaskForAlphaBank.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.kuzd4niil.testTaskForAlphaBank.clients.ExchangeRatesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class ExchangeRatesService {
    private ExchangeRatesClient exchangeRatesClient;
    @Value("${openexchangerates.appId}")
    private String appId;
    @Value("${openexchangerates.baseCurrency}")
    private String baseCurrency;

    public ExchangeRatesService(ExchangeRatesClient exchangeRatesClient) {
        this.exchangeRatesClient = exchangeRatesClient;
    }

    public boolean compareExchangeRatesTodayAndYesterday(String rateCurrency) {
        // Получаем курсы валют за сегодняшний день
        Object jsonExchangeRates = exchangeRatesClient.getTodayExchangeRates(appId, baseCurrency);

        Gson gson = new Gson();

        String json = gson.toJson(jsonExchangeRates);
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(json);

        Double todayRate = jsonObject.get("rates").getAsJsonObject().get(rateCurrency).getAsDouble();

        String dateFormatString = "yyyy-MM-dd";

        DateFormat dateFormat = new SimpleDateFormat(dateFormatString);

        Date yesterday = new Date(new Date().getTime() - (1000 * 60 * 60 * 24));

        // Получаем курсы валют за вчерашний день
        jsonExchangeRates = exchangeRatesClient.getSpecificDateExchangeRates(dateFormat.format(yesterday), appId, baseCurrency);

        json = gson.toJson(jsonExchangeRates);
        jsonObject = (JsonObject) JsonParser.parseString(json);

        Double yesterdayRate = jsonObject.get("rates").getAsJsonObject().get(rateCurrency).getAsDouble();

        return yesterdayRate < todayRate;
    }
}
