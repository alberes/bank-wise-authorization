package io.github.alberes.bank.wise.authorization.services;

import io.github.alberes.bank.wise.authorization.constants.Constants;
import io.github.alberes.bank.wise.authorization.domains.ClientAccount;
import io.github.alberes.bank.wise.authorization.repositories.ClientAccountRepository;
import io.github.alberes.bank.wise.authorization.services.exceptions.DuplicateRecordException;
import io.github.alberes.bank.wise.authorization.services.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientAccountService {

    private final ClientAccountRepository repository;

    private final PasswordEncoder encoder;

    @Transactional
    @Modifying
    public ClientAccount save(ClientAccount clientAccount){
        ClientAccount clientAccountDB = this.repository.findByLoginOrLegalEntityNumber(clientAccount.getLogin(),
                clientAccount.getLegalEntityNumber());
        if(clientAccountDB != null){
            DuplicateRecordException duplicateRecordException = new DuplicateRecordException(
                    Constants.REGISTRATION_WITH_LOGIN_OR_LEGAL_ENTITY_NUMBER + clientAccount.getLogin()
                    + Constants.SLASH + clientAccount.getLegalEntityNumber()
                    + Constants.SPACE
                    + Constants.HAS_ALREADY_BEEN_REGISTERED);
            log.error(duplicateRecordException.getMessage(), duplicateRecordException);
            throw duplicateRecordException;
        }
        String password = this.encoder.encode(clientAccount.getPassword());
        clientAccount.setPassword(password);
        clientAccount = this.repository.save(clientAccount);
        log.info("New Client account saved, clientAccountId: {}", clientAccount.getId());
        return clientAccount;
    }

    @Transactional(readOnly = true)
    public ClientAccount find(UUID id){
        Optional<ClientAccount> optional = this.repository.findById(id);
        log.info("Client account {}found. clientAccountId: {}", (optional.isPresent()? "" : "not "), id);
        return optional.orElseThrow(() -> new ObjectNotFoundException(
                Constants.OBJECT_NOT_FOUND_ID + id.toString() + Constants.TYPE + ClientAccount.class.getName()));
    }

    @Transactional
    @Modifying
    public void update(ClientAccount clientAccount){
        ClientAccount clientAccountDB = this.find(clientAccount.getId());
        clientAccountDB.setName(clientAccount.getName());
        this.repository.save(clientAccountDB);
        log.info("Updated clientAccountId {}", clientAccountDB.getId());
    }

    @Transactional
    @Modifying
    public void delete(UUID id){
        this.find(id);
        this.repository.deleteById(id);
        log.info("Client account deleted clientAccountId: {}", id);
    }
}
