package io.github.alberes.bank.wise.authorization.controllers;

import io.github.alberes.bank.wise.authorization.constants.Constants;
import io.github.alberes.bank.wise.authorization.controllers.dto.TransactionAccountDto;
import io.github.alberes.bank.wise.authorization.controllers.dto.TransactionDto;
import io.github.alberes.bank.wise.authorization.controllers.dto.TransactionReportDto;
import io.github.alberes.bank.wise.authorization.controllers.exceptions.AuthorizationException;
import io.github.alberes.bank.wise.authorization.controllers.exceptions.dto.StandardErrorDto;
import io.github.alberes.bank.wise.authorization.controllers.mappers.TransactionAccountBankStatementMapper;
import io.github.alberes.bank.wise.authorization.domains.ClientAccount;
import io.github.alberes.bank.wise.authorization.domains.TransactionAccount;
import io.github.alberes.bank.wise.authorization.domains.statements.BankStatement;
import io.github.alberes.bank.wise.authorization.services.TransactionAccountService;
import io.github.alberes.bank.wise.authorization.services.statements.BankStatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts/{id}/transactions")
@RequiredArgsConstructor
@Tag(name = "transaction")
public class TransactionAccountController implements GenericController{

    private final TransactionAccountService service;

    private final BankStatementService bankStatementService;

    private final TransactionAccountBankStatementMapper transactionAccountBankStatementMapper;

    @PostMapping
    @PreAuthorize(Constants.HAS_ROLE_ADMIN + Constants._OR_ + Constants.HAS_CLIENT_AUTHORITY_WRITE)
    @Operation(summary = "Save transaction.", description = "Save transaction in database.",
            operationId = "saveTransaction")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Saved with success."),
            @ApiResponse(responseCode = "400", description = "Validation error.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "409", description = "There is a Client with clientId.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<Void> save(@AuthenticationPrincipal Jwt jwt,
                                     @PathVariable String id, @RequestBody @Valid TransactionAccountDto dto){
        String clientAccountId = (String)jwt.getClaims().get("id");
        //Only Client Account must access data herself.
        if(!id.equals(clientAccountId)){
            AuthorizationException authorizationException = new AuthorizationException(HttpStatus.UNAUTHORIZED.value(),
                    HttpStatus.UNAUTHORIZED.getReasonPhrase());
            log.warn("Invalid access ClientAccountId: {} - id: {}", clientAccountId, id);
            throw authorizationException;
        }
        UUID uuid = UUID.fromString(id);
        ClientAccount clientAccount = new ClientAccount();
        clientAccount.setId(uuid);
        TransactionAccount transactionAccount = new TransactionAccount();
        transactionAccount.setClientAccount(clientAccount);
        transactionAccount
                .setTransactionType(this.service.getTransactionType(dto.type().toString()).getId());
        transactionAccount.setTransactionValue(dto.transactionValue());
        transactionAccount = this.service.save(transactionAccount);

        return ResponseEntity
                .created(this.createURI("/{id}", transactionAccount.getId().toString()))
                .build();
    }

    @GetMapping
    @PreAuthorize(Constants.HAS_ROLE_ADMIN_USER + Constants._OR_ + Constants.HAS_CLIENT_AUTHORITY_READ)
    @Operation(summary = "Find transactions.", description = "Find transactions in database.",
            operationId = "findClient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found transactions in database."),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Could not find transactions in database.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<TransactionReportDto> find(@AuthenticationPrincipal Jwt jwt, @PathVariable String id){
        BankStatement bankStatement = this.bankStatementService.find(id);
        if(bankStatement == null){
            return ResponseEntity.notFound().build();
        }
        if(bankStatement.getClientAccountBankStatement().getTransactions().isEmpty()){
            return ResponseEntity.noContent().build();
        }

        List<TransactionDto> transactions = bankStatement.getClientAccountBankStatement().getTransactions()
                .stream()
                .map(this.transactionAccountBankStatementMapper::toTransactionDto)
                .toList();

        TransactionReportDto report = new TransactionReportDto(bankStatement.getBalance(), transactions);

        return ResponseEntity.ok(report);

    }


}
