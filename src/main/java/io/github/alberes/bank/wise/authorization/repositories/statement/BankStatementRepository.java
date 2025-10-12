package io.github.alberes.bank.wise.authorization.repositories.statement;

import io.github.alberes.bank.wise.authorization.domains.statements.BankStatement;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BankStatementRepository extends MongoRepository<BankStatement, String> {
}
