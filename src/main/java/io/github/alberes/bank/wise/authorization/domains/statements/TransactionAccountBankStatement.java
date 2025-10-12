package io.github.alberes.bank.wise.authorization.domains.statements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAccountBankStatement {

    private String id;

    private String transactionType;

    private BigDecimal transactionValue;

    private LocalDateTime createdDate;

}
