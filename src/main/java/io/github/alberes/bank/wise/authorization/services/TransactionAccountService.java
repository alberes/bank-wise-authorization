package io.github.alberes.bank.wise.authorization.services;

import io.github.alberes.bank.wise.authorization.controllers.dto.TransactionDto;
import io.github.alberes.bank.wise.authorization.controllers.mappers.TransactionMapper;
import io.github.alberes.bank.wise.authorization.domains.ClientAccount;
import io.github.alberes.bank.wise.authorization.domains.TransactionAccount;
import io.github.alberes.bank.wise.authorization.enums.TransactionType;
import io.github.alberes.bank.wise.authorization.repositories.TransactionAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionAccountService {

    private final TransactionAccountRepository repository;

    private final ClientAccountService clientAccountService;

    private final TransactionMapper mapper;

    @Transactional
    @Modifying
    public TransactionAccount save(TransactionAccount transactionAccount){
        ClientAccount clientAccount = this.clientAccountService.find(transactionAccount.getClientAccount().getId());
        transactionAccount.setClientAccount(clientAccount);
        transactionAccount = this.repository.save(transactionAccount);

        List<TransactionAccount> transactions = this.repository
                .findByClientAccountIdOrderByCreatedDateAsc(transactionAccount.getClientAccount().getId());
        BigDecimal sum = transactions
                .stream()
                .map(TransactionAccount::getTransactionValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<TransactionDto> transactionsReport = transactions
                .stream()
                .map(this.mapper::toDto)
                .toList();

        return transactionAccount;
    }

    @Transactional
    public List<TransactionAccount> transactions(){
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
