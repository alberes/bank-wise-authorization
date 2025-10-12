package io.github.alberes.bank.wise.authorization.domains.statements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"transactions"})
public class ClientAccountBankStatement {

    private String id;

    public String name;

    public String legalEntityNumber;

    private String login;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    private List<TransactionAccountBankStatement> transactions;

}
