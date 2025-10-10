package io.github.alberes.bank.wise.authorization.controllers.exceptions.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(name = "StandardError")
public class StandardErrorDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long timestamp;

    private Integer status;

    private String error;

    private String message;

    private String path;

    private List<FieldErroDto> fields;

}