package io.github.alberes.bank.wise.authorization.services;

import io.github.alberes.bank.wise.authorization.controllers.mappers.ClientAccountBankStatementMapper;
import io.github.alberes.bank.wise.authorization.controllers.mappers.TransactionAccountBankStatementMapper;
import io.github.alberes.bank.wise.authorization.controllers.mappers.TransactionMapper;
import io.github.alberes.bank.wise.authorization.domains.ClientAccount;
import io.github.alberes.bank.wise.authorization.domains.TransactionAccount;
import io.github.alberes.bank.wise.authorization.domains.statements.BankStatement;
import io.github.alberes.bank.wise.authorization.domains.statements.ClientAccountBankStatement;
import io.github.alberes.bank.wise.authorization.domains.statements.TransactionAccountBankStatement;
import io.github.alberes.bank.wise.authorization.enums.TransactionType;
import io.github.alberes.bank.wise.authorization.repositories.TransactionAccountRepository;
import io.github.alberes.bank.wise.authorization.services.statements.BankStatementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionAccountService {

    private final TransactionAccountRepository repository;

    private final ClientAccountService clientAccountService;

    private final TransactionMapper mapper;

    private final TransactionAccountBankStatementMapper transactionAccountBankStatementMapper;

    private final ClientAccountBankStatementMapper clientAccountBankStatementMapper;

    private final BankStatementService bankStatementService;

    private final InterestRateService interestRateService;

    @Transactional
    @Modifying
    public TransactionAccount save(TransactionAccount transactionAccount){
        ClientAccount clientAccount = this.clientAccountService.find(transactionAccount.getClientAccount().getId());
        transactionAccount.setClientAccount(clientAccount);
        transactionAccount = this.repository.save(transactionAccount);

        //MongoDB
        BankStatement bankStatement = this.bankStatementService.find(clientAccount.getId().toString());

        //First transaction
        if(bankStatement == null){
            bankStatement = new BankStatement();
            bankStatement.setId(clientAccount.getId().toString());
            ClientAccountBankStatement clientAccountBankStatement =
                    clientAccountBankStatementMapper.toClientAccountBankStatement(clientAccount);
            bankStatement.setBalance(BigDecimal.ZERO);
            bankStatement.setClientAccountBankStatement(clientAccountBankStatement);
            bankStatement.getClientAccountBankStatement().setTransactions(new ArrayList<TransactionAccountBankStatement>());
        }

        //Prepare new transaction in MongoDB
        //Need to sse the bank balance to calculate interest and create new transaction.
        TransactionAccountBankStatement transactionAccountBankStatement = null;
        if(transactionAccount.getTransactionType().equals(TransactionType.DEPOSIT.getId())) {
            TransactionAccount transactionAccountInterest = this.interestRateService.createInterest(bankStatement, clientAccount);
            if (transactionAccountInterest != null) {
                transactionAccountInterest = this.repository.save(transactionAccountInterest);
                transactionAccountBankStatement =
                        transactionAccountBankStatementMapper.toTransactionAccountBankStatement(transactionAccountInterest);
                bankStatement.getClientAccountBankStatement()
                        .getTransactions().add(transactionAccountBankStatement);
            }
        }

        //Add current transaction
        transactionAccountBankStatement =
                transactionAccountBankStatementMapper.toTransactionAccountBankStatement(transactionAccount);
        bankStatement.getClientAccountBankStatement()
                .getTransactions().add(transactionAccountBankStatement);

        //Create or update in MongoDB
        BigDecimal deposits = bankStatement.depositBalance();
        BigDecimal payments = bankStatement.paymentBalance();
        BigDecimal bankBalance = deposits.subtract(payments);
        log.info("AccoutId: {} - deposit: {} - payment: {} - balance: {}", bankStatement.getId(), deposits, payments, bankBalance);
        bankStatement.setBalance(bankBalance);
        bankStatement = this.bankStatementService.save(bankStatement);

        return transactionAccount;
    }

    @Transactional(readOnly = true)
    public List<TransactionAccount> transactions(UUID id){
        return null;
    }

    public TransactionType getTransactionType(String description){
        if(TransactionType.DEPOSIT.getDescription().equals(description)){
            return TransactionType.DEPOSIT;
        }else if(TransactionType.WITHDRAW.getDescription().equals(description)){
            return TransactionType.WITHDRAW;
        }else if(TransactionType.PAYMENT.getDescription().equals(description)){
            return TransactionType.PAYMENT;
        }else{
            return null;
        }
    }

}
