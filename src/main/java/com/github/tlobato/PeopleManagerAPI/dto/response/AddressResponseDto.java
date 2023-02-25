package com.github.tlobato.PeopleManagerAPI.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDto {

    private Long id;
    private String street;
    private String city;
    private String zipCode;
    private String houseNumber;
    private boolean mainAddress;
}