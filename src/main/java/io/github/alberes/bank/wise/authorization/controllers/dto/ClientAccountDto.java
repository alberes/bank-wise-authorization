package io.github.alberes.bank.wise.authorization.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Set;

@Schema(name = "ClientAccount")
public record ClientAccountDto(
        @NotBlank(message = "Obligatory field")
        @Size(min = 10, max = 100, message = "Fill this field with size between 10 and 100")
        String name,

        @NotBlank(message = "Obligatory field")
        @Size(min = 11, max = 11, message = "Fill this field with size 11")
        @CPF(message = "Legal entity number invalid")
        String legalEntityNumber,

        @NotBlank(message = "Obligatory field")
        @Size(min = 8, max = 100, message = "Fill this field with size between 8 and 100")
        String login,

        @NotBlank(message = "Obligatory field")
        @Size(min = 4, max = 20, message = "Fill this field with size between 4 and 20")
        String password,

        @NotEmpty(message = "Scope list cannot be empty")
        @Valid
        Set<String> scopes) {
}