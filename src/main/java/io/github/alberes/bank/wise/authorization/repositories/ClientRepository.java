package io.github.alberes.bank.wise.authorization.repositories;

import io.github.alberes.bank.wise.authorization.domains.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    public Client findByClientId(String clientId);

    @Transactional
    public void deleteByClientId(String clientId);

}