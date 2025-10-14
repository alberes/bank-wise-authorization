package io.github.alberes.bank.wise.authorization.controllers;

import io.github.alberes.bank.wise.authorization.constants.Constants;
import io.github.alberes.bank.wise.authorization.controllers.dto.ClientAccountDto;
import io.github.alberes.bank.wise.authorization.controllers.dto.ClientAccountUpdateDto;
import io.github.alberes.bank.wise.authorization.controllers.exceptions.dto.StandardErrorDto;
import io.github.alberes.bank.wise.authorization.controllers.mappers.ClientAccountMapper;
import io.github.alberes.bank.wise.authorization.domains.ClientAccount;
import io.github.alberes.bank.wise.authorization.services.ClientAccountService;
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

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "ClientAccount")
public class ClientAccountController implements GenericController{

    private final ClientAccountService service;

    private final ClientAccountMapper mapper;

    @PostMapping
    @PreAuthorize(Constants.HAS_ROLE_ADMIN)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Saved with success."),
            @ApiResponse(responseCode = "400", description = "Validation error.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "409", description = "There is a client account with login.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<Void> save(@RequestBody @Valid ClientAccountDto dto){
        ClientAccount clientAccount = this.mapper.toEntity(dto);
        Set<String> scopes = dto
                .scopes()
                .stream()
                .map(String::toUpperCase)
                .collect(Collectors.toSet());
        clientAccount.setScopes(scopes);
        clientAccount = this.service.save(clientAccount);
        return ResponseEntity
                .created(this.createURI("/{id}", clientAccount.getId().toString()))
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize(Constants.HAS_ROLE_ADMIN)
    @Operation(summary = "Find client account.", description = "Find client account in database. Client account with profile ADMIN can access any users and other profiles can only access resources that belong to him.",
            operationId = "findClientAccount")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found user in database."),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Could not find user in database.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<ClientAccountDto> find(@PathVariable String id){
        UUID userId = UUID.fromString(id);
        ClientAccount clientAccount = this.service.find(userId);
        clientAccount.setPassword(null);
        ClientAccountDto dto = this.mapper.toDto(clientAccount);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize(Constants.HAS_ROLE_ADMIN)
    @Operation(summary = "Update client account.", description = "Update with success. Client account with profile ADMIN can access any users and other profiles can only access resources that belong to him.",
            operationId = "updateClientAccount")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Update name with success."),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Could not find user in database.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody @Valid ClientAccountUpdateDto dto){
        UUID userId = UUID.fromString(id);
        ClientAccount clientAccount = new ClientAccount();
        clientAccount.setId(userId);
        clientAccount.setName(dto.name());
        this.service.update(clientAccount);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(Constants.HAS_ROLE_ADMIN)
    @Operation(summary = "Delete client account.", description = "Delete with success. Client account with profile ADMIN can access any users and other profiles can only access resources that belong to him.",
            operationId = "deleteClientAccount")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Delete client account in database."),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Could not find client account in database.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<Void> delete(@PathVariable String id){
        UUID userId = UUID.fromString(id);
        this.service.delete(userId);
        return ResponseEntity.noContent().build();
    }

}
