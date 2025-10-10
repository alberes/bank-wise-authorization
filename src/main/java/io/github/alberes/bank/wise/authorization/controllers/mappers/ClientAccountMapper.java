package io.github.alberes.bank.wise.authorization.controllers.mappers;

import io.github.alberes.bank.wise.authorization.controllers.dto.ClientAccountDto;
import io.github.alberes.bank.wise.authorization.domains.ClientAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientAccountMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "login", target = "login")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "scopes", target = "scopes")
    public ClientAccount toEntity(ClientAccountDto dto);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "login", target = "login")
    public ClientAccountDto toDto(ClientAccount clientAccount);
}
