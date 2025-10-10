package io.github.alberes.bank.wise.authorization.services;

import io.github.alberes.bank.wise.authorization.constants.Constants;
import io.github.alberes.bank.wise.authorization.domains.ClientAccount;
import io.github.alberes.bank.wise.authorization.domains.UserPrincipal;
import io.github.alberes.bank.wise.authorization.repositories.ClientAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPrincipalDetailsService implements UserDetailsService {

    private final ClientAccountRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ClientAccount clientAccount = this.repository.findByLogin(username);

        if(clientAccount == null){
            UsernameNotFoundException usernameNotFoundException = new UsernameNotFoundException(Constants.OBJECT_NOT_FOUND);
            log.error(usernameNotFoundException.getMessage(), usernameNotFoundException);
            throw usernameNotFoundException;
        }
        return new UserPrincipal(clientAccount);
    }
}
