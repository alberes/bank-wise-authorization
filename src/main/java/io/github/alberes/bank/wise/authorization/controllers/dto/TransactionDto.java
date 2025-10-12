package io.github.alberes.bank.wise.authorization.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(String type,
                             @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
                             BigDecimal valor,
                             @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
                             LocalDateTime data) {
}
