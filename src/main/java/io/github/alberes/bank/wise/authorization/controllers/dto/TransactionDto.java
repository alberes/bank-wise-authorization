package io.github.alberes.bank.wise.authorization.controllers.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(String type, BigDecimal valor, LocalDateTime data) {
}
