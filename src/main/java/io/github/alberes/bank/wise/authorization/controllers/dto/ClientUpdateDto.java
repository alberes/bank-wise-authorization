package io.github.alberes.bank.wise.authorization.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientUpdateDto(
        @NotBlank(message = "Obligatory field")
        @Size(min = 10, max = 500, message = "Fill this field with size between 10 and 500")
        String redirectURI) {
}