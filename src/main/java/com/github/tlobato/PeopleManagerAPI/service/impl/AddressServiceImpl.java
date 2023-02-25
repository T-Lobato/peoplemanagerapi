package com.github.tlobato.PeopleManagerAPI.service.impl;

import com.github.tlobato.PeopleManagerAPI.dto.request.AddressRequestDto;
import com.github.tlobato.PeopleManagerAPI.dto.response.AddressResponseDto;
import com.github.tlobato.PeopleManagerAPI.entities.Person;
import com.github.tlobato.PeopleManagerAPI.entities.PersonAddresses;
import com.github.tlobato.PeopleManagerAPI.exception.NotFoundException;
import com.github.tlobato.PeopleManagerAPI.integrations.ViaCepApi;
import com.github.tlobato.PeopleManagerAPI.repository.AddressRepository;
import com.github.tlobato.PeopleManagerAPI.service.AddressService;
import com.github.tlobato.PeopleManagerAPI.service.PersonService;
import com.github.tlobato.PeopleManagerAPI.shared.enums.ErrorCode;
import java.util.List;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final ModelMapper mapper;

    private final AddressRepository addressRepository;

    private final PersonService personService;

    private final ViaCepApi viaCepApi;

    @Override
    @Transactional
    public AddressResponseDto create(Long personId, AddressRequestDto addressRequestDto) {

        Person person = mapper.map(personService.findPersonById(personId), Person.class);

        AddressRequestDto addressAux = viaCepApi.getCompleteAddress(addressRequestDto.getCep());
        PersonAddresses personAddresses = new PersonAddresses();

        personAddresses.setZipCode(addressRequestDto.getCep());
        personAddresses.setCity(addressAux.getLocalidade());
        personAddresses.setStreet(addressAux.getLogradouro());
        personAddresses.setHouseNumber(addressRequestDto.getHouseNumber());
        personAddresses.setPerson(person);

        ensuringOneMainsAddressAtTime(addressRequestDto, person, personAddresses);

        return mapper.map(addressRepository.save(personAddresses), AddressResponseDto.class);
    }

    @Override
    public List<AddressResponseDto> getAddresses(Long personId) {
        personService.findPersonById(personId);
        List<PersonAddresses> addresses = addressRepository.findAllByPersonId(personId);
        return addresses.stream()
                .map(address -> mapper.map(address, AddressResponseDto.class)).toList();
    }

    @Override
    @Transactional
    public AddressResponseDto updateMainAddress(Long personId, Long addressId) {
        PersonAddresses personAddresses = addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.EC002.getMessage(), ErrorCode.EC002.getCode()));
        Person person = mapper.map(personService.findPersonById(personId), Person.class);

        personAddresses.getPerson().getPersonAddressesList()
                .forEach(n -> {
                    n.setMainAddress(false);
                    n.setPerson(person);
                    addressRepository.save(n);
                });
        personAddresses.setMainAddress(true);
        return mapper.map(addressRepository.save(personAddresses), AddressResponseDto.class);
    }

    @Override
    public void ensuringOneMainsAddressAtTime(AddressRequestDto addressRequestDto, Person person, PersonAddresses personAddresses) {
        if (Boolean.TRUE.equals(addressRequestDto.isMainAddress())) {
            person.getPersonAddressesList()
                    .forEach(n -> {
                        n.setMainAddress(false);
                        n.setPerson(person);
                        addressRepository.save(n);
                    });
            personAddresses.setMainAddress(true);
        }
    }
}