package org.kuzd4niil.testTaskForAlphaBank.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.kuzd4niil.testTaskForAlphaBank.clients.ExchangeRatesClient;
import org.kuzd4niil.testTaskForAlphaBank.clients.GifJsonClient;
import org.kuzd4niil.testTaskForAlphaBank.clients.WebpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

@Service
public class ExchangeRatesService {
    private ExchangeRatesClient exchangeRatesClient;
    private GifJsonClient gifClient;
    private WebpClient webpClient;
    @Value("${giphy.apiKey}")
    private String apiKey;
    @Value("${openExchangeRates.appId}")
    private String appId;
    @Value("${openExchangeRates.baseCurrency}")
    private String baseCurrency;
    @Value("${giphy.mediaEndpoint}")
    private String giphyMediaEndpoint;

    private static Set<String> currencyCodeSet = new HashSet<>();

    public ExchangeRatesService(ExchangeRatesClient exchangeRatesClient, GifJsonClient gifClient, WebpClient webpClient) {
        this.exchangeRatesClient = exchangeRatesClient;
        this.gifClient = gifClient;
        this.webpClient = webpClient;

        if (currencyCodeSet.isEmpty()) {
            LinkedHashMap<String, String> currencies = exchangeRatesClient.getCurrencies();
            currencyCodeSet = currencies.keySet();
        }
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

    public byte[] getImage(String rateCurrency) throws IOException {
        if (!currencyCodeSet.contains(rateCurrency)) {
            return new FileInputStream(ResourceUtils.getFile("classpath:cherry.webp")).readAllBytes();
        }

        Object jsonGif = gifClient.getRandomGif(apiKey, compareExchangeRatesTodayAndYesterday(rateCurrency) ? "rich" : "broke");

        Gson gson = new Gson();

        String json = gson.toJson(jsonGif);
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(json);

        byte[] webp = webpClient.getWebp(jsonObject.get("data").getAsJsonObject().get("id").getAsString());
        return webp;
    }

    public String getURLImage(String rateCurrency) {
        if (!currencyCodeSet.contains(rateCurrency)) {
            return "https://simpl.info/webp/cherry.webp";
        }

        Object jsonGif = gifClient.getRandomGif(apiKey, compareExchangeRatesTodayAndYesterday(rateCurrency) ? "rich" : "broke");

        Gson gson = new Gson();

        String json = gson.toJson(jsonGif);
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(json);

        String gifId = jsonObject.get("data").getAsJsonObject().get("id").getAsString();

        return giphyMediaEndpoint + "/" + gifId + "/giphy.webp";
    }
}
