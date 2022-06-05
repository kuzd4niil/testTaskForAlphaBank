package org.kuzd4niil.testTaskForAlphaBank;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WireMockTest(httpPort = 9561)
public class TestTaskForAlphaBankApplicationTests {

	MockMvc mvc;
//	@Value("${openExchangeRates.appId}")
	static String appId = "bd0b1b8081c848bf98d690dc6c0bb2e8";
//	@Value("${giphy.apiKey}")
	static String apiKey = "Y9hegm0TadfWGDJUiExrTWJTjciR34g2";
	static String expectedContentType;
	static String expectedRedirectUrl;
	static byte[] expectedImage;
	static byte[] cherryImage;
	public static byte[] rateCurrencyYesterday;
	public static byte[] rateCurrencyToday;
	public static byte[] richGifJson;
	public static byte[] brokeGifJson;
	static byte[] richGif;
	static byte[] brokeGif;

	@BeforeAll
	public static void initWireMock() throws IOException {
		expectedContentType = "image/webp";
		expectedRedirectUrl = "https://simpl.info/webp/cherry.webp";
		expectedImage = new FileInputStream(ResourceUtils.getFile("classpath:cherry.webp")).readAllBytes();
		// Можно в принципе удалить. Повторяет expectedImage
		cherryImage = new FileInputStream(ResourceUtils.getFile("classpath:cherry.webp")).readAllBytes();

		rateCurrencyYesterday = new FileInputStream(ResourceUtils.getFile("classpath:rateCurrencyYesterday.json")).readAllBytes();
		rateCurrencyToday = new FileInputStream(ResourceUtils.getFile("classpath:rateCurrencyToday.json")).readAllBytes();
		richGifJson = new FileInputStream(ResourceUtils.getFile("classpath:richGifJson.json")).readAllBytes();
		brokeGifJson = new FileInputStream(ResourceUtils.getFile("classpath:brokeGifJson.json")).readAllBytes();
		richGif = new FileInputStream(ResourceUtils.getFile("classpath:rich.gif")).readAllBytes();
		brokeGif = new FileInputStream(ResourceUtils.getFile("classpath:broke.gif")).readAllBytes();

		// Заглушка для получения кодов валют (для Feign)
		stubFor(get(urlEqualTo("/api/currencies.json"))
				.willReturn(ok()
						.withHeader("Content-Type", "application/json")
						.withBody(new FileInputStream(ResourceUtils.getFile("classpath:currencies.json")).readAllBytes())));
	}

	@BeforeEach
	public void initCustomStub() throws IOException {
		// Заглушки для получения курса валют за вчера и сегодня (для Feign)
		String dateFormatString = "yyyy-MM-dd";
		DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		Date yesterday = new Date(new Date().getTime() - (1000 * 60 * 60 * 24));
		String yesterdayDate = dateFormat.format(yesterday);

		// Если звпрос на вчерашний день, то возвращаем rateCurrencyYesterday.json
		stubFor(get(urlPathEqualTo("/api/historical/" + yesterdayDate + ".json"))
				.withQueryParam("app_id", equalTo(appId))
				.withQueryParam("base_currency", equalTo("USD"))
				.willReturn(ok()
						.withHeader("Content-Type", "application/json")
						.withBody(rateCurrencyYesterday)));
		// Если запрос на сегодняшний день, то возвращаем rateCurrencyToday.json
		stubFor(get(urlPathEqualTo("/api/latest.json"))
				.withQueryParam("app_id", equalTo(appId))
				.withQueryParam("base_currency", equalTo("USD"))
				.willReturn(ok()
						.withHeader("Content-Type", "application/json")
						.withBody(rateCurrencyToday)));

		// Заглушки для получения JSON GIF-изображений
		stubFor(get(urlPathEqualTo("/v1/gifs/random"))
				.withQueryParam("api_key", equalTo(apiKey))
				.withQueryParam("tag", equalTo("rich"))
				.willReturn(ok()
						.withHeader("Content-Type", "application/json")
						.withBody(richGifJson)));
		stubFor(get(urlPathEqualTo("/v1/gifs/random"))
				.withQueryParam("api_key", equalTo(apiKey))
				.withQueryParam("tag", equalTo("broke"))
				.willReturn(ok()
						.withHeader("Content-Type", "application/json")
						.withBody(brokeGifJson)));

		// Заглушки для получения самих изображений
		stubFor(get(urlEqualTo("/media/rich/giphy.webp"))
				.willReturn(ok()
						.withHeader("Content-Type", "image/webp")
						.withBody(richGif)));
		stubFor(get(urlEqualTo("/media/broke/giphy.webp"))
				.willReturn(ok()
						.withHeader("Content-Type", "image/webp")
						.withBody(brokeGif)));
	}

	@Autowired
	public TestTaskForAlphaBankApplicationTests(MockMvc mvc) throws IOException {
		this.mvc = mvc;
	}

	@Test
	@DisplayName("Отправка несуществующего кода валюты, должны получить статическое изображение")
	public void testShouldGetStaticImageWhenSendInvalidCurrencyCode() throws Exception {
		// Передаём не существующую валюту.
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/compare_rate")
						.queryParam("rate_currency", "Invalid currency"))
				.andExpect(status().isOk()).andReturn();

		// Извлекаем из ответа тип контента и изображение.
		String contentType = result.getResponse().getContentType();
		byte[] resultingImage = result.getResponse().getContentAsByteArray();

		// Тип контента должен быть "image/webp", само изображение должно совпадать с cherry.webp
		assertTrue(expectedContentType.equals(contentType));
		assertTrue(Arrays.equals(resultingImage, expectedImage));
	}

	@Test
	@DisplayName("Отправка корректного кода валюты, должны получить GIF-изображение rich.gif")
	public void testShouldGetRichGifWhenSendValidCurrencyCode() throws Exception {
		// Передаём существующую валюту.
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/compare_rate")
						.queryParam("rate_currency", "RUB"))
				.andExpect(status().isOk()).andReturn();

		// Извлекаем из ответа тип контента и изображение.
		String contentType = result.getResponse().getContentType();
		byte[] resultingImage = result.getResponse().getContentAsByteArray();

		// Тип контента должен быть "image/webp", само изображение должно не совпадать с cherry.webp
		assertTrue(expectedContentType.equals(contentType));
		assertFalse(Arrays.equals(resultingImage, expectedImage));
	}

	@Test
	@DisplayName("Отправка корректного кода валюты EUR, должны получить GIF-изображение broke.gif")
	public void testShouldGetBrokeGifWhenSendValidCurrencyCode() throws Exception {
		// Передаём существующую валюту.
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/compare_rate")
						.queryParam("rate_currency", "EUR"))
				.andExpect(status().isOk()).andReturn();

		// Извлекаем из ответа тип контента и изображение.
		String contentType = result.getResponse().getContentType();
		byte[] resultingImage = result.getResponse().getContentAsByteArray();

		// Тип контента должен быть "image/webp", само изображение должно не совпадать с cherry.webp
		assertTrue(expectedContentType.equals(contentType));
		assertFalse(Arrays.equals(resultingImage, expectedImage));
	}

	@Test
	@DisplayName("Отправка несуществующего кода валюты, должны получить redirect на статическое изображение")
	public void testShouldGetRedirectToStaticImageWhenSendInvalidCurrencyCode() throws Exception {
		// Передаём не существующую валюту.
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/redirect_compare_rate")
						.queryParam("rate_currency", "Invalid currency"))
				.andExpect(status().isFound()).andReturn();

		// Должны получить redirect, который совпадает с redirect на статическую картинку
		String redirectUrl = result.getResponse().getRedirectedUrl();
		assertTrue(expectedRedirectUrl.equals(redirectUrl));
	}

	@Test
	@DisplayName("Отправка корректного кода валюты, должны получить redirect на GIF-изображение")
	public void testShouldGetRedirectToGifWhenSendValidCurrencyCode() throws Exception {
		// Передаём существующую валюту. Должны получить перенаправление на <не cherry>.webp
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/redirect_compare_rate")
						.queryParam("rate_currency", "RUB"))
				.andExpect(status().isFound()).andReturn();

		// Должны получить redirect, который не совпадает с redirect на статическую картинку
		String redirectUrl = result.getResponse().getRedirectedUrl();
		assertFalse(expectedRedirectUrl.equals(redirectUrl));
	}
}
