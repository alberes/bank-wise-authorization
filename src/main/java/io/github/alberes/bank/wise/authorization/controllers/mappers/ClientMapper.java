package io.github.alberes.bank.wise.authorization.controllers.mappers;

import io.github.alberes.bank.wise.authorization.controllers.dto.ClientDto;
import io.github.alberes.bank.wise.authorization.domains.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(source = "clientId", target = "clientId")
    @Mapping(source = "clientSecret", target = "clientSecret")
    @Mapping(source = "redirectURI", target = "redirectURI")
    @Mapping(source = "scope", target = "scope")
    public Client toEntity(ClientDto dto);

    @Mapping(source = "clientId", target = "clientId")
    @Mapping(source = "clientSecret", target = "clientSecret")
    @Mapping(source = "redirectURI", target = "redirectURI")
    @Mapping(source = "scope", target = "scope")
    public ClientDto toDto(Client client);

}
