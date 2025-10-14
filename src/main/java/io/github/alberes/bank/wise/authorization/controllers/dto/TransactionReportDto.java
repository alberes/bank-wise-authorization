package io.github.alberes.bank.wise.authorization.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.List;

public record TransactionReportDto(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#,##0.00")
        BigDecimal bankBalance, List<TransactionDto> historico) {
}
