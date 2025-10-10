package io.github.alberes.bank.wise.authorization.controllers.dto;

import java.math.BigDecimal;

public record TransactionReportDto(BigDecimal saldoTotal, TransactionDto historico) {
}
