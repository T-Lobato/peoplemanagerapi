package com.github.tlobato.PeopleManagerAPI.shared;

import com.github.tlobato.PeopleManagerAPI.exception.InvalidInputException;
import com.github.tlobato.PeopleManagerAPI.exception.NotFoundException;
import com.github.tlobato.PeopleManagerAPI.shared.dto.ErrorResponseDto;
import com.github.tlobato.PeopleManagerAPI.shared.dto.FieldErrorResponseDto;
import com.github.tlobato.PeopleManagerAPI.shared.enums.ErrorCode;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponseDto handleNotFoundException(NotFoundException exception, WebRequest request) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto();

        errorResponseDto.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponseDto.setMessage(exception.getMessage());
        errorResponseDto.setInternalCode(exception.getErrorCode());
        errorResponseDto.setPath(request.getDescription(false));
        errorResponseDto.setTimestamp(LocalDateTime.now());

        return errorResponseDto;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SQLException.class)
    public ErrorResponseDto handleNotFoundException(SQLException exception, WebRequest request) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto();

        errorResponseDto.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseDto.setMessage(ErrorCode.EC201.getMessage());
        errorResponseDto.setInternalCode(ErrorCode.EC201.getCode());
        errorResponseDto.setPath(request.getDescription(false));
        errorResponseDto.setTimestamp(LocalDateTime.now());

        return errorResponseDto;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidInputException.class)
    public ErrorResponseDto handleNotFoundException(InvalidInputException exception, WebRequest request) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto();

        errorResponseDto.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseDto.setMessage(ErrorCode.EC102.getMessage());
        errorResponseDto.setInternalCode(ErrorCode.EC102.getCode());
        errorResponseDto.setPath(request.getDescription(false));
        errorResponseDto.setTimestamp(LocalDateTime.now());

        return errorResponseDto;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ErrorResponseDto httpMessageNotReadableException(HttpMessageNotReadableException exception, WebRequest request) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto();

        errorResponseDto.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseDto.setMessage(ErrorCode.EC202.getMessage());
        errorResponseDto.setInternalCode(ErrorCode.EC202.getCode());
        errorResponseDto.setPath(request.getDescription(false));
        errorResponseDto.setTimestamp(LocalDateTime.now());

        return errorResponseDto;

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest request) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto();

        errorResponseDto.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseDto.setMessage(ErrorCode.EC101.getMessage());
        errorResponseDto.setInternalCode(ErrorCode.EC101.getCode());
        errorResponseDto.setErros(exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> new FieldErrorResponseDto(e.getDefaultMessage(), e.getField()))
                .toList());
        errorResponseDto.setPath(request.getDescription(false));
        errorResponseDto.setTimestamp(LocalDateTime.now());

        return errorResponseDto;

    }
}