package com.github.tlobato.PeopleManagerAPI.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonRequestDto {

    @JsonIgnore
    private Long id;

    @NotBlank(message = "name is a required field!")
    private String name;
    @Past(message = "must be a past date!")
    private LocalDate birthDate;
}