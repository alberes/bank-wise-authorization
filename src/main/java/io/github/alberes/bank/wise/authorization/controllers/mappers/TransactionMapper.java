package io.github.alberes.bank.wise.authorization.controllers.mappers;

import io.github.alberes.bank.wise.authorization.controllers.dto.TransactionDto;
import io.github.alberes.bank.wise.authorization.domains.TransactionAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    //@Mapping(source = "tipo", target = "type")
    //@Mapping(source = "valor", target = "transactionValue")
    //@Mapping(source = "data", target = "java(transactionAccount.getMapTransactionType())")
    public TransactionDto toDto(TransactionAccount transactionAccount);

}
