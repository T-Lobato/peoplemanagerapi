package com.github.tlobato.PeopleManagerAPI.controller;

import com.github.tlobato.PeopleManagerAPI.dto.request.AddressRequestDto;
import com.github.tlobato.PeopleManagerAPI.dto.request.PersonRequestDto;
import com.github.tlobato.PeopleManagerAPI.dto.response.AddressResponseDto;
import com.github.tlobato.PeopleManagerAPI.dto.response.PersonResponseDto;
import com.github.tlobato.PeopleManagerAPI.service.AddressService;
import com.github.tlobato.PeopleManagerAPI.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Person Controller")
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/person")
public class PersonController {

    private final PersonService personService;

    private final AddressService addressService;

    private final ModelMapper mapper;

    @Operation(summary = "Create a person.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonResponseDto createPerson(@RequestBody @Valid PersonRequestDto personRequestDto) {
        return personService.createPerson(personRequestDto);
    }

    @Operation(summary = "Find a person by ID.")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponseDto findPersonById(@PathVariable Long id) {
        return personService.findPersonById(id);
    }

    @Operation(summary = "Find all people.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PersonResponseDto> findAllPeople() {
        return personService.findAllPeople();
    }

    @Operation(summary = "Update a person.")
    @PutMapping("/{personId}")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponseDto updatePerson(@PathVariable Long personId, @RequestBody @Valid PersonRequestDto personRequestDto) {
        return personService.updatePerson(personId, personRequestDto);
    }

    @Operation(summary = "Create an address.")
    @PostMapping("/{personId}/addresses")
    @ResponseStatus(HttpStatus.CREATED)
    public AddressResponseDto createAddress(@PathVariable Long personId, @RequestBody @Valid AddressRequestDto addressRequestDto) {
        return mapper.map(addressService.create(personId, addressRequestDto), AddressResponseDto.class);
    }

    @Operation(summary = "Find all addresses from a person.")
    @GetMapping("/{personId}/addresses")
    @ResponseStatus(HttpStatus.OK)
    public List<AddressResponseDto> getAddresses(@PathVariable Long personId) {
        return addressService.getAddresses(personId);
    }

    @Operation(summary = "Update main address.")
    @PatchMapping("/{personId}/addresses/{addressId}")
    @ResponseStatus(HttpStatus.OK)
    public AddressResponseDto updateMainAddress(@PathVariable Long personId, @PathVariable Long addressId) {
        return addressService.updateMainAddress(personId, addressId);
    }
}