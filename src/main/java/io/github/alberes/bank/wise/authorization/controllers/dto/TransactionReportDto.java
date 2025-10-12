package io.github.alberes.bank.wise.authorization.controllers.dto;

import java.math.BigDecimal;
import java.util.List;

public record TransactionReportDto(BigDecimal saldoTotal, List<TransactionDto> historico) {
}
