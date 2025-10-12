package io.github.alberes.bank.wise.authorization.controllers.mappers;

import io.github.alberes.bank.wise.authorization.domains.ClientAccount;
import io.github.alberes.bank.wise.authorization.domains.statements.ClientAccountBankStatement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientAccountBankStatementMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "legalEntityNumber", target = "legalEntityNumber")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "lastModifiedDate", target = "lastModifiedDate")
    public ClientAccountBankStatement toClientAccountBankStatement(ClientAccount clientAccount);
}
