package com.github.tlobato.PeopleManagerAPI.service;

import com.github.tlobato.PeopleManagerAPI.dto.request.AddressRequestDto;
import com.github.tlobato.PeopleManagerAPI.dto.response.AddressResponseDto;
import com.github.tlobato.PeopleManagerAPI.entities.Person;
import com.github.tlobato.PeopleManagerAPI.entities.PersonAddresses;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {

    AddressResponseDto create(Long personId, AddressRequestDto addressRequestDto);

    List<AddressResponseDto> getAddresses(Long personId);

    AddressResponseDto updateMainAddress(Long personId, Long addressId);

    void ensuringOneMainsAddressAtTime(AddressRequestDto addressRequestDto, Person person, PersonAddresses personAddresses);
}