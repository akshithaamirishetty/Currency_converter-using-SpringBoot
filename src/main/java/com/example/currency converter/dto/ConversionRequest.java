package com.example.currencyconverter.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ConversionRequest(

    @NotBlank
    String from,

    @NotBlank
    String to,

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal amount

) {}