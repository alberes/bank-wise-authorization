package io.github.alberes.bank.wise.authorization.repositories;

import io.github.alberes.bank.wise.authorization.domains.TransactionAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface TransactionAccountRepository extends JpaRepository<TransactionAccount, UUID> {

    @Transactional(readOnly = true)
    public List<TransactionAccount> findByClientAccountIdOrderByCreatedDateAsc(UUID clientAccountId);

}
