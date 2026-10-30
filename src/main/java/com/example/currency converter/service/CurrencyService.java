package com.example.currencyconverter.service;

import com.example.currencyconverter.dto.ExchangeRateResponse;
import com.example.currencyconverter.model.ConversionHistory;
import com.example.currencyconverter.repository.ConversionHistoryRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class CurrencyService {

    private static final String API =
            "https://api.frankfurter.app";

    private final RestClient restClient;
    private final ConversionHistoryRepository repository;

    public CurrencyService(
        RestClient restClient,
        ConversionHistoryRepository repository
    ) {
        this.restClient = restClient;
        this.repository = repository;
    }

    public Map<String, String> getCurrencies() {

        @SuppressWarnings("unchecked")
        Map<String, String> currencies =
            restClient.get()
                .uri(API + "/currencies")
                .retrieve()
                .body(Map.class);

        return currencies;
    }

    public ExchangeRateResponse getRates(String base) {

        String currency = normalize(base);

        try {
            return restClient.get()
                .uri(API + "/latest?from={base}", currency)
                .retrieve()
                .body(ExchangeRateResponse.class);

        } catch (RestClientException e) {
            throw new IllegalArgumentException(
                "Unable to retrieve exchange rates."
            );
        }
    }

    public ConversionHistory convert(
        String from,
        String to,
        BigDecimal amount
    ) {

        String source = normalize(from);
        String target = normalize(to);

        if (source.equals(target)) {

            return repository.save(
                new ConversionHistory(
                    source,
                    target,
                    amount,
                    BigDecimal.ONE,
                    amount.setScale(2, RoundingMode.HALF_UP)
                )
            );
        }

        try {

            ExchangeRateResponse response =
                restClient.get()
                    .uri(API + "/latest?from={from}&to={to}",
                         source, target)
                    .retrieve()
                    .body(ExchangeRateResponse.class);

            if (response == null ||
                response.rates() == null ||
                !response.rates().containsKey(target)) {

                throw new IllegalArgumentException(
                    "Unsupported currency."
                );
            }

            BigDecimal rate = response.rates().get(target);

            BigDecimal converted =
                amount.multiply(rate)
                    .setScale(2, RoundingMode.HALF_UP);

            ConversionHistory history =
                new ConversionHistory(
                    source,
                    target,
                    amount,
                    rate,
                    converted
                );

            return repository.save(history);

        } catch (RestClientException e) {

            throw new IllegalArgumentException(
                "Exchange rate API is unavailable or currency is invalid."
            );
        }
    }

    private String normalize(String currency) {

        if (currency == null ||
            !currency.matches("(?i)[a-z]{3}")) {

            throw new IllegalArgumentException(
                "Enter a valid 3-letter currency code."
            );
        }

        return currency.toUpperCase();
    }
}