package com.safarov.tech_app.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.safarov.tech_app.service.CurrencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CurrencyController {
    CurrencyService currencyService;

    @GetMapping("/currencies")
    public ResponseEntity<?> getCurrencies() {
        return new ResponseEntity<>(currencyService.getCurrencies(), HttpStatus.OK);
    }
}
