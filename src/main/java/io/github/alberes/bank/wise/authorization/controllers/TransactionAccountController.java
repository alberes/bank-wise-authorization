package io.github.alberes.bank.wise.authorization.controllers;

import io.github.alberes.bank.wise.authorization.constants.Constants;
import io.github.alberes.bank.wise.authorization.controllers.dto.TransactionAccountDto;
import io.github.alberes.bank.wise.authorization.controllers.exceptions.dto.StandardErrorDto;
import io.github.alberes.bank.wise.authorization.domains.ClientAccount;
import io.github.alberes.bank.wise.authorization.domains.TransactionAccount;
import io.github.alberes.bank.wise.authorization.enums.TransactionType;
import io.github.alberes.bank.wise.authorization.services.TransactionAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts/{id}/transactions")
@RequiredArgsConstructor
@Tag(name = "transaction")
public class TransactionAccountController implements GenericController{

    private final TransactionAccountService service;

    @PostMapping
    @PreAuthorize(Constants.HAS_ROLE_ADMIN + Constants._OR_ + Constants.HAS_CLIENT_AUTHORITY_WRITE)
    @Operation(summary = "Save deposit.", description = "Save deposit in database.",
            operationId = "saveDeposit")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Saved with success."),
            @ApiResponse(responseCode = "400", description = "Validation error.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "409", description = "There is a Client with clientId.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<Void> save(@PathVariable String id, @RequestBody @Valid TransactionAccountDto dto){
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


}
