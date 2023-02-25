package com.github.tlobato.PeopleManagerAPI.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class FieldErrorResponseDto {

    private String message;
    private String field;
}
