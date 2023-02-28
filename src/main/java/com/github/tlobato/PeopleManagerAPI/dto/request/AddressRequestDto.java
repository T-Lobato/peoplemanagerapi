package com.github.tlobato.PeopleManagerAPI.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private String logradouro;

    @JsonIgnore
    private String localidade;

    @NotBlank(message = "cep is a required field!")
    private String cep;

    @NotBlank(message = "houseNumber is a required field!")
    private String houseNumber;

    @JsonIgnore
    private Long personId;

    private boolean mainAddress;
}