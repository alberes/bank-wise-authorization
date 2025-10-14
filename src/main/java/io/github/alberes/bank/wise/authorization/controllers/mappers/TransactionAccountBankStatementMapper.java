package io.github.alberes.bank.wise.authorization.controllers.mappers;

import io.github.alberes.bank.wise.authorization.controllers.dto.TransactionDto;
import io.github.alberes.bank.wise.authorization.domains.TransactionAccount;
import io.github.alberes.bank.wise.authorization.domains.statements.TransactionAccountBankStatement;
import io.github.alberes.bank.wise.authorization.enums.TransactionType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionAccountBankStatementMapper {

    @Mapping(source = "transactionType", target = "transactionType")
    @Mapping(source = "transactionValue", target = "transactionValue")
    @Mapping(source = "createdDate", target = "createdDate")
    public TransactionAccountBankStatement toTransactionAccountBankStatement(TransactionAccount transactionAccount);

    @Mapping(source = "transactionType", target = "transactionType")
    @Mapping(source = "transactionValue", target = "transactionValue")
    @Mapping(source = "createdDate", target = "createdDate")
    public TransactionDto toTransactionDto(TransactionAccountBankStatement transactionAccountBankStatement);

    default String mapTransactionTypeToTransactionType(Integer id){
        TransactionType transactionType = TransactionType.getTransactionType(id);
        if(transactionType == null){
            return "UNDEFINED";
        }
        return transactionType.getDescription();
    }
}
