package io.github.alberes.bank.wise.authorization.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientDto(
        @NotBlank(message = "Obligatory field")
        @Size(min = 10, max = 100, message = "Fill this field with size between 10 and 100")
        String clientId,
        @NotBlank(message = "Obligatory field")
        @Size(min = 10, max = 100, message = "Fill this field with size between 10 and 100")
        String clientSecret,
        @NotBlank(message = "Obligatory field")
        @Size(min = 10, max = 500, message = "Fill this field with size between 10 and 500")
        String redirectURI,
        @NotBlank(message = "Obligatory field")
        @Size(min = 4, max = 100, message = "Fill this field with size between 4 and 100")
        String scope) {
}