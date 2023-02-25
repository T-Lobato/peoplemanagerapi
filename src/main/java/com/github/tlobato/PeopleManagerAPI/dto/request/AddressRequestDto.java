package com.github.tlobato.PeopleManagerAPI.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestDto {

    private String logradouro;
    private String localidade;

    @NotBlank(message = "cep is a required field!")
    private String cep;

    @NotBlank(message = "houseNumber is a required field!")
    private String houseNumber;

    private Long personId;
    private boolean mainAddress;
}