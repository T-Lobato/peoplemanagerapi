package com.github.tlobato.PeopleManagerAPI.shared.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    EC001("EC-001", "This person does not exists."),
    EC002("EC-002", "This address does not exists."),
    EC101("EC-101", "Invalid Request."),
    EC102("EC-102", "Invalid input - The zip code entered is not valid."),
    EC201("EC-201", "birthDate is a required field."),
    EC202("EC-202", "mainAddress is a required field, please enter one of the following values: [true or false]");

    private final String code;
    private final String message;
}