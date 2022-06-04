package org.kuzd4niil.testTaskForAlphaBank.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.kuzd4niil.testTaskForAlphaBank.services.ExchangeRatesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("api/v1")
public class ExchangeRatesController {

    private ExchangeRatesService exchangeRatesService;

    public ExchangeRatesController(ExchangeRatesService exchangeRatesService) {
        this.exchangeRatesService = exchangeRatesService;
    }

    @Operation(summary = "Получить GIF-изображение, сравнив курс валют относительно ${openexchangerates.baseCurrency} за сегодня и вчера.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GIF-изображение",
            content = {@Content(mediaType = "image/webp",
            schema = @Schema(implementation = byte[].class))})
    })
    @GetMapping(value = "compare_rate", produces = "image/webp")
    public byte[] compareExchangeRatesAndGetGif(
            @Parameter(description = "Код валюты, курс которой будет сравниваться")
            @RequestParam(value = "rate_currency") String rateCurrency
    ) throws IOException {
        byte[] image = exchangeRatesService.getImage(rateCurrency);
        return image;
    }

    @Operation(summary = "В отличии от compare_rate сразу перенаправит на необходимое изображение, не скачивая его на сервер")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GIF-изображение",
                    content = {@Content(mediaType = "image/webp",
                            schema = @Schema(implementation = byte[].class))})
    })
    @GetMapping(value = "redirect_compare_rate", produces = "image/webp")
    public RedirectView compareExchangeRatesAndGetGifWithRedirectToResource(
            @Parameter(description = "Код валюты, курс которой будет сравниваться")
            @RequestParam(value = "rate_currency") String rateCurrency
    ) {
        String url = exchangeRatesService.getURLImage(rateCurrency);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);

        return redirectView;
    }
}
