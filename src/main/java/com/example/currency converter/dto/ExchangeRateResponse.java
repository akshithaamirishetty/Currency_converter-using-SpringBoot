package com.example.currencyconverter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExchangeRateResponse(
    String amount,
    String base,
    String date,
    Map<String, BigDecimal> rates
) {}