package com.github.tlobato.PeopleManagerAPI.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonResponseDto {

    private Long id;
    private String name;
    private LocalDate birthDate;
    private List<AddressResponseDto> addresses;
}