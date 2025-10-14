package io.github.alberes.bank.wise.authorization.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.alberes.bank.wise.authorization.constants.Constants;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(String transactionType,
                             @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#,##0.00")
                             BigDecimal transactionValue,
                             @JsonFormat(pattern= Constants.DATE_TIME_FORMATTER_PATTERN)
                             LocalDateTime createdDate) {
}
