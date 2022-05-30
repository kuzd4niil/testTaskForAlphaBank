package org.kuzd4niil.testTaskForAlphaBank.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.kuzd4niil.testTaskForAlphaBank.clients.GifJsonClient;
import org.kuzd4niil.testTaskForAlphaBank.clients.WebpClient;
import org.springframework.stereotype.Service;

@Service
public class GifService {
    private GifJsonClient gifClient;
    private WebpClient webpClient;

    public GifService(GifJsonClient gifClient, WebpClient webpClient) {
        this.gifClient = gifClient;
        this.webpClient = webpClient;
    }

    public byte[] getImage(String tag) {
        Object gif = gifClient.getRandomGif("Y9hegm0TadfWGDJUiExrTWJTjciR34g2", "rich");

        Gson gson = new Gson();

        String json = gson.toJson(gif);
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(json);

        byte[] webpImage = webpClient.getWebp(jsonObject.get("data").getAsJsonObject().get("id").getAsString());

        return webpImage;
    }
}
