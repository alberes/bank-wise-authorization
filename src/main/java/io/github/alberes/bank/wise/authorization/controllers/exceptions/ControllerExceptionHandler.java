package io.github.alberes.bank.wise.authorization.controllers.exceptions;

import io.github.alberes.bank.wise.authorization.controllers.exceptions.dto.FieldErroDto;
import io.github.alberes.bank.wise.authorization.controllers.exceptions.dto.StandardErrorDto;
import io.github.alberes.bank.wise.authorization.services.exceptions.DuplicateRecordException;
import io.github.alberes.bank.wise.authorization.services.exceptions.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<StandardErrorDto> duplicateRecordException(DuplicateRecordException duplicateRecordException,
                                                                     HttpServletRequest httpServletRequest){

        StandardErrorDto standardErrorDto = new StandardErrorDto(
                System.currentTimeMillis(), HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(), duplicateRecordException.getMessage(), httpServletRequest.getRequestURI(),
                List.of()
        );

        return ResponseEntity
                .status(standardErrorDto.getStatus())
                .body(standardErrorDto);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardErrorDto> objectNotFoundException(ObjectNotFoundException objectNotFoundException,
                                                                    HttpServletRequest httpServletRequest){
        StandardErrorDto standardErrorDto = new StandardErrorDto(
                System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(), objectNotFoundException.getMessage(), httpServletRequest.getRequestURI(),
                List.of()
        );

        return ResponseEntity
                .status(standardErrorDto.getStatus())
                .body(standardErrorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardErrorDto> methodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException,
                                                                            HttpServletRequest httpServletRequest){
        List<FieldError> fieldErrors = methodArgumentNotValidException.getFieldErrors();

        List<FieldErroDto> fieldErrorsDto = fieldErrors.stream()
                .map(fd -> new FieldErroDto(fd.getField(), fd.getDefaultMessage()))
                .toList();
        StandardErrorDto standardErrorDto = new StandardErrorDto(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), methodArgumentNotValidException.getMessage(),
                httpServletRequest.getRequestURI(),
                fieldErrorsDto);

        return ResponseEntity
                .status(standardErrorDto.getStatus()).body(standardErrorDto);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardErrorDto> httpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException,
                                                                            HttpServletRequest httpServletRequest){
        StandardErrorDto standardErrorDto = new StandardErrorDto(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                httpMessageNotReadableException.getMessage(),
                httpServletRequest.getRequestURI(),
                List.of());

        return ResponseEntity
                .status(standardErrorDto.getStatus()).body(standardErrorDto);
    }

}
