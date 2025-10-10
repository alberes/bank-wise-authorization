package io.github.alberes.bank.wise.authorization.domains;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

    @Getter
    private final ClientAccount clientAccount;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.clientAccount.getScopes()
                .stream().map(role -> new SimpleGrantedAuthority(role))
                .toList();
        /*return this.userAccount.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority(role))
                .toList();*/
    }

    @Override
    public String getPassword() {
        return this.clientAccount.getPassword();
    }

    @Override
    public String getUsername() {
        return this.clientAccount.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}