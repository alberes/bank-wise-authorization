package io.github.alberes.bank.wise.authorization.services;

import io.github.alberes.bank.wise.authorization.constants.Constants;
import io.github.alberes.bank.wise.authorization.domains.Client;
import io.github.alberes.bank.wise.authorization.repositories.ClientRepository;
import io.github.alberes.bank.wise.authorization.services.exceptions.DuplicateRecordException;
import io.github.alberes.bank.wise.authorization.services.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository repository;

    private final PasswordEncoder encoder;

    @Transactional
    @Modifying
    public Client save(Client client){
        Client clientDB = this.repository.findByClientId(client.getClientId());
        if(clientDB != null){
            DuplicateRecordException duplicateRecordException = new DuplicateRecordException(
                    Constants.REGISTRATION_WITH_E_CLIENT_ID + client.getClientId() + Constants.HAS_ALREADY_BEEN_REGISTERED);
            log.error(duplicateRecordException.getMessage(), duplicateRecordException);
            throw duplicateRecordException;
        }

        String secret = this.encoder.encode(client.getClientSecret());
        client.setClientSecret(secret);
        client = this.repository.save(client);
        log.info("New client, id: {}", client.getId());
        return client;
    }

    @Transactional(readOnly = true)
    public Client find(String clientId){
        Client client = this.repository.findByClientId(clientId);

        if(client == null){
            log.info("Client not found. ClientId: {}", clientId);
            throw new ObjectNotFoundException(
                    Constants.OBJECT_NOT_FOUND_ID + clientId + Constants.TYPE + Client.class.getName());
        }
        return client;
    }

    @Transactional
    @Modifying
    public void update(Client client){
        Client clientDB = this.find(client.getClientId());
        clientDB.setRedirectURI(client.getRedirectURI());
        this.repository.save(clientDB);
        log.info("Updated client {}", clientDB.getClientId());
    }

    @Transactional
    @Modifying
    public void delete(String clientId){
        this.find(clientId);
        this.repository.deleteByClientId(clientId);
        log.info("User deleted clientId: {}", clientId);
    }

}
