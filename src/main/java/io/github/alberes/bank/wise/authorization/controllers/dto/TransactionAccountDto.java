package io.github.alberes.bank.wise.authorization.controllers.dto;

import io.github.alberes.bank.wise.authorization.controllers.dto.enums.TransactionTypeDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionAccountDto(
        @NotNull(message = "Obligatory field")
        TransactionTypeDto type,
        @Positive(message = "The transactionValue must be greater than 0.")
        BigDecimal transactionValue) {
}
