package io.github.alberes.bank.wise.authorization.services;

import io.github.alberes.bank.wise.authorization.domains.ClientAccount;
import io.github.alberes.bank.wise.authorization.domains.TransactionAccount;
import io.github.alberes.bank.wise.authorization.domains.statements.BankStatement;
import io.github.alberes.bank.wise.authorization.enums.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class InterestRateService {

    @Value("${app.interestRate}")
    private BigDecimal interestRate;

    private static final BigDecimal PERCENT = BigDecimal.valueOf(100);

    public BigDecimal calcInterest(BigDecimal value){
        return value
                .multiply(interestRate)
                .divide(PERCENT)
                .abs();
    }

    public TransactionAccount createInterest(BankStatement bankStatement, ClientAccount clientAccount){
        TransactionAccount transactionAccountInterest = null;

        BigDecimal deposits = bankStatement.depositBalance();
        BigDecimal payments = bankStatement.paymentBalance();
        BigDecimal bankBalance = deposits.subtract(payments);

        if(bankBalance.compareTo(BigDecimal.ZERO) < 0){
            BigDecimal interest = this.calcInterest(bankBalance);
            transactionAccountInterest = new TransactionAccount();
            transactionAccountInterest.setClientAccount(clientAccount);
            transactionAccountInterest.setTransactionType(TransactionType.INTEREST.getId());
            transactionAccountInterest.setTransactionValue(interest);
            bankBalance = bankBalance.subtract(payments) ;
        }
        return transactionAccountInterest;
    }
}
