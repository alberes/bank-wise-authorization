package io.github.alberes.bank.wise.authorization.repositories;

import io.github.alberes.bank.wise.authorization.domains.ClientAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientAccountRepository extends JpaRepository<ClientAccount, UUID> {

    public ClientAccount findByLogin(String login);

    public ClientAccount findByLoginOrLegalEntityNumber(String login, String legalEntityNumber);

}
