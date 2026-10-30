package com.example.currencyconverter.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversion_history")
public class ConversionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromCurrency;
    private String toCurrency;

    private BigDecimal amount;
    private BigDecimal exchangeRate;
    private BigDecimal convertedAmount;

    private LocalDateTime createdAt;

    protected ConversionHistory() {}

    public ConversionHistory(
        String fromCurrency,
        String toCurrency,
        BigDecimal amount,
        BigDecimal exchangeRate,
        BigDecimal convertedAmount
    ) {

        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
        this.exchangeRate = exchangeRate;
        this.convertedAmount = convertedAmount;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}