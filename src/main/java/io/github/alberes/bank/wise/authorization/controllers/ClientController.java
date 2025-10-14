package io.github.alberes.bank.wise.authorization.controllers;

import io.github.alberes.bank.wise.authorization.constants.Constants;
import io.github.alberes.bank.wise.authorization.controllers.dto.ClientDto;
import io.github.alberes.bank.wise.authorization.controllers.dto.ClientUpdateDto;
import io.github.alberes.bank.wise.authorization.controllers.exceptions.dto.StandardErrorDto;
import io.github.alberes.bank.wise.authorization.controllers.mappers.ClientMapper;
import io.github.alberes.bank.wise.authorization.domains.Client;
import io.github.alberes.bank.wise.authorization.services.ClientService;
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

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Tag(name = "Client")
public class ClientController implements GenericController{

    private final ClientService service;

    private final ClientMapper mapper;

    @PostMapping
    @PreAuthorize(Constants.HAS_ROLE_ADMIN)
    @Operation(summary = "Save client.", description = "Save client in database. Only client with profile ADMIN can create client.",
            operationId = "saveClient")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Saved with success."),
            @ApiResponse(responseCode = "400", description = "Validation error.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "409", description = "There is a Client with clientId.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<Void> save(@RequestBody @Valid ClientDto dto){
        Client client = this.mapper.toEntity(dto);
        this.service.save(client);

        return ResponseEntity
                .created(this.createURI("/{id}", client.getId().toString()))
                .build();
    }

    @GetMapping("/{clientId}")
    @PreAuthorize(Constants.HAS_ROLE_ADMIN)
    @Operation(summary = "Find client.", description = "Find client in database. Client with profile ADMIN can access any clients and other profiles can only access resources that belong to him.",
            operationId = "findClient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found client in database."),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Could not find client in database.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<ClientDto> find(@PathVariable String clientId){
        Client client = this.service.find(clientId);
        client.setClientSecret(null);
        ClientDto dto = this.mapper.toDto(client);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{clientId}")
    @PreAuthorize(Constants.HAS_ROLE_ADMIN)
    @Operation(summary = "Update client.", description = "Update with success. Client with profile ADMIN can access any clients and other profiles can only access resources that belong to him.",
            operationId = "updateClient")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Update with success."),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Could not find clint in database.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<Void> update(@PathVariable String clientId, @RequestBody @Valid ClientUpdateDto dto){
        Client client = new Client();
        client.setClientId(clientId);
        client.setRedirectURI(dto.redirectURI());
        this.service.update(client);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{clientId}")
    @PreAuthorize(Constants.HAS_ROLE_ADMIN)
    @Operation(summary = "Delete client.", description = "Delete with success. User with profile ADMIN can access any clients and other profiles can only access resources that belong to him.",
            operationId = "deleteClient")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Delete client in database."),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Could not find client in database.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<Void> delete(@PathVariable String clientId){
        this.service.delete(clientId);
        return ResponseEntity.noContent().build();
    }

}
