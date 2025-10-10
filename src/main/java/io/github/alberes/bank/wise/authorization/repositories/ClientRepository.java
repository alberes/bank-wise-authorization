package io.github.alberes.bank.wise.authorization.repositories;

import io.github.alberes.bank.wise.authorization.domains.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    public Client findByClientId(String clientId);

    public void deleteByClientId(String clientId);

}