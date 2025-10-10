package io.github.alberes.bank.wise.authorization.repositories;

import io.github.alberes.bank.wise.authorization.domains.TransactionAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface TransactionAccountRepository extends JpaRepository<TransactionAccount, UUID> {

    //@Transactional(readOnly = true)
    //@Query("SELECT transaction FROM TransactionAccount transactionAccount WHERE transaction.clientAccount.id = :clientAccountId ORDER BY transaction.createdDate DESC")
    //public List<TransactionAccount> findTransactions(@Param("clientAccountId") UUID clientAccountId);

    public List<TransactionAccount> findByClientAccountIdOrderByCreatedDateAsc(UUID clientAccountId);

}
