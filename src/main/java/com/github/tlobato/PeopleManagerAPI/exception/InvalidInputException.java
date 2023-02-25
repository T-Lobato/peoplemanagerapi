package com.github.tlobato.PeopleManagerAPI.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidInputException extends RuntimeException {

    private String message;
    private String errorCode;
}