package com.example.currencyconverter.controller;

import com.example.currencyconverter.dto.*;
import com.example.currencyconverter.model.ConversionHistory;
import com.example.currencyconverter.repository.ConversionHistoryRepository;
import com.example.currencyconverter.service.CurrencyService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CurrencyController {

    private final CurrencyService service;
    private final ConversionHistoryRepository repository;

    public CurrencyController(
        CurrencyService service,
        ConversionHistoryRepository repository
    ) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping("/currencies")
    public Map<String, String> currencies() {
        return service.getCurrencies();
    }

    @GetMapping("/rates")
    public ExchangeRateResponse rates(
        @RequestParam(defaultValue = "USD") String base
    ) {
        return service.getRates(base);
    }

    @PostMapping("/convert")
    @ResponseStatus(HttpStatus.CREATED)
    public ConversionHistory convert(
        @Valid @RequestBody ConversionRequest request
    ) {
        return service.convert(
            request.from(),
            request.to(),
            request.amount()
        );
    }

    @GetMapping("/history")
    public List<ConversionHistory> history() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/history/{id}")
    public ConversionHistory getHistory(@PathVariable Long id) {

        return repository.findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "History record not found"
                )
            );
    }

    @DeleteMapping("/history/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHistory(@PathVariable Long id) {

        if (!repository.existsById(id)) {
            throw new IllegalArgumentException(
                "History record not found"
            );
        }

        repository.deleteById(id);
    }
}