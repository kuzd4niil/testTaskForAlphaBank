package org.kuzd4niil.testTaskForAlphaBank;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
//@WireMockTest(httpPort = 8081)
@AutoConfigureMockMvc
//@EnableConfigurationProperties
//@TestPropertySource("classpath:test-application.yml")
class TestTaskForAlphaBankApplicationTests {

	MockMvc mvc;
	byte[] expectedImage;
	String expectedContentType;
	String expectedRedirectUrl;
	RestTemplate restTemplate;

	@Autowired
	public TestTaskForAlphaBankApplicationTests(MockMvc mvc) throws IOException {
		this.mvc = mvc;
		expectedImage = new FileInputStream(ResourceUtils.getFile("classpath:cherry.webp")).readAllBytes();
		expectedContentType = "image/webp";
		expectedRedirectUrl = "https://simpl.info/webp/cherry.webp";
		restTemplate = new RestTemplate();
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
	@DisplayName("Отправка корректного кода валюты, должны получить GIF-изображение")
	public void testShouldGetGifWhenSendValidCurrencyCode() throws Exception {
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
	@DisplayName("Отправка несуществующего кода валюты, должны получить redirect на статическое изображение")
	public void testShouldGetRedirectToStaticImageWhenSendInvalidCurrencyCode() throws Exception {
		// Передаём не существующую валюту.
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/redirect_compare_rate")
						.queryParam("rate_currency", "Invalid currency"))
				.andExpect(status().isFound()).andReturn();

		// Должны получить redirect, который совпадает с redirect на статическую картинку
		String redirectUrl = result.getResponse().getRedirectedUrl();
		assertTrue(expectedRedirectUrl.equals(redirectUrl));

		// Делаем запрос на внешний сервис
		ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(redirectUrl, byte[].class);

		// Извлекаем из ответа тип контента и изображение.
		String contentType = responseEntity.getHeaders().getContentType().toString();
		byte[] resultingImage = responseEntity.getBody();

		// Тип контента должен быть "image/webp", само изображение должно совпадать с cherry.webp
		assertTrue(expectedContentType.equals(contentType));
		assertTrue(Arrays.equals(resultingImage, expectedImage));
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

		// Делаем запрос на внешний сервис
		ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(redirectUrl, byte[].class);

		// Извлекаем из ответа тип контента и изображение.
		String contentType = responseEntity.getHeaders().getContentType().toString();
		byte[] resultingImage = responseEntity.getBody();

		// Тип контента должен быть "image/webp", само изображение должно не совпадать с cherry.webp
		assertTrue(expectedContentType.equals(contentType));
		assertFalse(Arrays.equals(resultingImage, expectedImage));
	}
}
