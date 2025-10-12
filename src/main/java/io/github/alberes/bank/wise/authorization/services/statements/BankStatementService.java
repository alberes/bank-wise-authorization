package io.github.alberes.bank.wise.authorization.services.statements;

import io.github.alberes.bank.wise.authorization.domains.statements.BankStatement;
import io.github.alberes.bank.wise.authorization.repositories.statement.BankStatementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankStatementService {

    private final BankStatementRepository repository;

    public BankStatement save(BankStatement bankStatement){
        return this.repository.save(bankStatement);
    }

    public BankStatement find(String id){
        return this.repository.findById(id)
                .orElse(null);
    }
}
