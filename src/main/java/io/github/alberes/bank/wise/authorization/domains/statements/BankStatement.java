package io.github.alberes.bank.wise.authorization.domains.statements;

import io.github.alberes.bank.wise.authorization.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankStatement {

    @Id
    private String id;

    private BigDecimal balance;

    public BigDecimal depositBalance(){
        if(this.getClientAccountBankStatement() == null){
            return BigDecimal.ZERO;
        }else if(this.getClientAccountBankStatement().getTransactions() == null
                || this.getClientAccountBankStatement().getTransactions().isEmpty()){
            return BigDecimal.ZERO;
        }
        return this.getClientAccountBankStatement()
                .getTransactions()
                .stream()
                .filter(t -> t.getTransactionType().equals(TransactionType.DEPOSIT.getDescription()))
                .map(TransactionAccountBankStatement::getTransactionValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal paymentBalance(){
        if(this.getClientAccountBankStatement() == null){
            return BigDecimal.ZERO;
        }else if(this.getClientAccountBankStatement().getTransactions() == null
            || this.getClientAccountBankStatement().getTransactions().isEmpty()){
            return BigDecimal.ZERO;
        }
        return this.getClientAccountBankStatement()
                .getTransactions()
                .stream()
                .filter(t -> !t.getTransactionType().equals(TransactionType.DEPOSIT.getDescription()))
                .map(TransactionAccountBankStatement::getTransactionValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private ClientAccountBankStatement clientAccountBankStatement;

}
