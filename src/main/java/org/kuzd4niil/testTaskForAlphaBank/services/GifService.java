package org.kuzd4niil.testTaskForAlphaBank.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.kuzd4niil.testTaskForAlphaBank.clients.GifJsonClient;
import org.kuzd4niil.testTaskForAlphaBank.clients.WebpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GifService {
    private GifJsonClient gifClient;
    private WebpClient webpClient;
    @Value("${giphy.apiKey}")
    private String apiKey;

    public GifService(GifJsonClient gifClient, WebpClient webpClient) {
        this.gifClient = gifClient;
        this.webpClient = webpClient;
    }

    public byte[] getImage(String tag) {
        Object jsonGif = gifClient.getRandomGif(apiKey, tag);

        Gson gson = new Gson();

        String json = gson.toJson(jsonGif);
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(json);

        return webpClient.getWebp(jsonObject.get("data").getAsJsonObject().get("id").getAsString());
    }
}
