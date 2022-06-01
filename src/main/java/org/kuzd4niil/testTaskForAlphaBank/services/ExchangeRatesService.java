package org.kuzd4niil.testTaskForAlphaBank.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.kuzd4niil.testTaskForAlphaBank.clients.ExchangeRatesClient;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ExchangeRatesService {
    private ExchangeRatesClient exchangeRatesClient;

    public ExchangeRatesService(ExchangeRatesClient exchangeRatesClient) {
        this.exchangeRatesClient = exchangeRatesClient;
    }

    public boolean compareExchangeRatesTodayAndYesterday(String rateCurrency) {
        Object jsonExchangeRates = exchangeRatesClient.getTodayExchangeRates("bd0b1b8081c848bf98d690dc6c0bb2e8", "USD");

        Gson gson = new Gson();

        String json = gson.toJson(jsonExchangeRates);
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(json);

        Double todayRate = jsonObject.get("rates").getAsJsonObject().get(rateCurrency).getAsDouble();

        String dateFormatString = "yyyy-MM-dd";

        DateFormat dateFormat = new SimpleDateFormat(dateFormatString);

        jsonExchangeRates = exchangeRatesClient.getSpecificDateExchangeRates(dateFormat.format(new Date()), "bd0b1b8081c848bf98d690dc6c0bb2e8", "USD");

        json = gson.toJson(jsonExchangeRates);
        jsonObject = (JsonObject) JsonParser.parseString(json);

        Double yesterdayRate = jsonObject.get("rates").getAsJsonObject().get(rateCurrency).getAsDouble();

        return yesterdayRate < todayRate;
    }
}
