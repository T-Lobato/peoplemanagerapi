package com.github.tlobato.PeopleManagerAPI.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.tlobato.PeopleManagerAPI.dto.request.AddressRequestDto;
import com.github.tlobato.PeopleManagerAPI.dto.request.PersonRequestDto;
import com.github.tlobato.PeopleManagerAPI.dto.response.AddressResponseDto;
import com.github.tlobato.PeopleManagerAPI.dto.response.PersonResponseDto;
import com.github.tlobato.PeopleManagerAPI.entities.Person;
import com.github.tlobato.PeopleManagerAPI.entities.PersonAddresses;
import com.github.tlobato.PeopleManagerAPI.integrations.ViaCepApi;
import com.github.tlobato.PeopleManagerAPI.repository.AddressRepository;
import com.github.tlobato.PeopleManagerAPI.repository.PersonRepository;
import com.github.tlobato.PeopleManagerAPI.service.AddressService;
import com.github.tlobato.PeopleManagerAPI.service.PersonService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    private final Long PERSON_ID = 1L;

    private final LocalDate BIRTHDATE = LocalDate.of(1988, 12, 13);

    private PersonRepository personRepository;

    private AddressRepository addressRepository;

    private PersonService personService;

    private AddressService addressService;

    private ViaCepApi viaCepApi;

    private ModelMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new ModelMapper();
        personRepository = mock(PersonRepository.class);
        addressRepository = mock(AddressRepository.class);
        viaCepApi = mock(ViaCepApi.class);
        personService = mock(PersonService.class);
        addressService = new AddressServiceImpl(mapper, addressRepository, personService, viaCepApi);
    }

    private PersonRequestDto buildPersonRequestDto() {
        return PersonRequestDto.builder()
                .name("Thyago")
                .birthDate(BIRTHDATE)
                .build();
    }

    private PersonResponseDto buildPersonResponseDto() {
        return PersonResponseDto.builder()
                .name("Thyago")
                .birthDate(BIRTHDATE)
                .build();
    }

    private Person buildPerson() {
        return Person.builder()
                .name("Thyago")
                .birthDate(BIRTHDATE)
                .build();
    }

    private AddressRequestDto buildAddressRequestDto() {
        return AddressRequestDto.builder()
                .cep("18040040")
                .houseNumber("500")
                .build();
    }

    private PersonAddresses buildPersonAddresses() {
        return PersonAddresses.builder()
                .street("Rua Ana Augusto")
                .city("Sorocaba")
                .zipCode("18040040")
                .houseNumber("500")
                .mainAddress(false)
                .build();
    }

    @Test
    @DisplayName("Must successfully receive an addressRequestDto save a address and return an addressResponseDto")
    void createAddressTest() {

        Person person = buildPerson();
        person.setId(PERSON_ID);

        AddressRequestDto addressRequestDto = buildAddressRequestDto();

        AddressRequestDto addressAux = AddressRequestDto.builder()
                .logradouro("Rua Ana Augusto")
                .localidade("Sorocaba")
                .build();

        when(viaCepApi.getCompleteAddress(addressRequestDto.getCep())).thenReturn(addressAux);

        viaCepApi.getCompleteAddress(addressRequestDto.getCep());

        verify(viaCepApi).getCompleteAddress(addressRequestDto.getCep());

        PersonAddresses savedAddress = PersonAddresses.builder()
                .street(addressAux.getLogradouro())
                .city(addressAux.getLocalidade())
                .zipCode(addressRequestDto.getCep())
                .houseNumber(addressRequestDto.getHouseNumber())
                .mainAddress(addressRequestDto.isMainAddress())
                .person(person)
                .build();

        when(addressRepository.save(any())).thenReturn(savedAddress);
        when(personService.findPersonById(person.getId())).thenReturn(mapper.map(person, PersonResponseDto.class));

        addressService.create(person.getId(), addressRequestDto);

        verify(addressRepository).save(any());

        assertEquals(savedAddress.getStreet(), addressAux.getLogradouro());
        assertEquals(savedAddress.getCity(), addressAux.getLocalidade());
        assertEquals(savedAddress.getZipCode(), addressRequestDto.getCep());
        assertEquals(savedAddress.getHouseNumber(), addressRequestDto.getHouseNumber());
        assertEquals(savedAddress.isMainAddress(), addressRequestDto.isMainAddress());
    }

    @Test
    @DisplayName("Must successfully retrieve a list of AddressResponseDto for a given person")
    void getAddressesTest() {

        Person person = buildPerson();
        person.setId(PERSON_ID);

        List<PersonAddresses> addresses = Arrays.asList(buildPersonAddresses(), buildPersonAddresses());

        when(personService.findPersonById(person.getId())).thenReturn(mapper.map(person, PersonResponseDto.class));

        when(addressRepository.findAllByPersonId(person.getId())).thenReturn(addresses);

        List<AddressResponseDto> result = addressService.getAddresses(person.getId());

        verify(personService).findPersonById(person.getId());

        verify(addressRepository).findAllByPersonId(person.getId());

        List<AddressResponseDto> expected = addresses.stream()
                .map(address -> mapper.map(address, AddressResponseDto.class)).toList();

        assertEquals(expected.get(0).getCity(), result.get(0).getCity());
        assertEquals(expected.get(0).getStreet(), result.get(0).getStreet());
        assertEquals(expected.get(0).getZipCode(), result.get(0).getZipCode());
        assertEquals(expected.get(0).getHouseNumber(), result.get(0).getHouseNumber());
        assertEquals(expected.get(0).isMainAddress(), result.get(0).isMainAddress());
        assertEquals(expected.get(1).getCity(), result.get(1).getCity());
        assertEquals(expected.get(1).getStreet(), result.get(1).getStreet());
        assertEquals(expected.get(1).getZipCode(), result.get(1).getZipCode());
        assertEquals(expected.get(1).getHouseNumber(), result.get(1).getHouseNumber());
        assertEquals(expected.get(1).isMainAddress(), result.get(1).isMainAddress());
    }

    @Test
    @DisplayName("Must successfully update the main address for a given person")
    void updateMainAddressTest() {

        Person person = buildPerson();
        person.setId(PERSON_ID);

        PersonAddresses address1 = buildPersonAddresses();
        address1.setId(1L);
        address1.setPerson(person);

        PersonAddresses address2 = buildPersonAddresses();
        address2.setId(2L);
        address2.setPerson(person);

        address2.setMainAddress(true);

        person.setPersonAddressesList(Arrays.asList(address1, address2));

        when(addressRepository.findById(1L)).thenReturn(Optional.of(address1));
        when(addressRepository.save(any(PersonAddresses.class))).thenReturn(address1);
        when(personService.findPersonById(person.getId())).thenReturn(mapper.map(person, PersonResponseDto.class));

        AddressResponseDto result = addressService.updateMainAddress(person.getId(), 1L);

        assertTrue(result.isMainAddress());

        assertFalse(address2.isMainAddress());
    }

    @Test
    @DisplayName("Should make one address as true and set all others to false.")
    void ensuringOneMainsAddressAtTimeTest() {
        Person person = new Person();
        List<PersonAddresses> addresses = new ArrayList<>();

        PersonAddresses personAddressToMain = new PersonAddresses();
        personAddressToMain.setMainAddress(false);

        PersonAddresses personAddressToNotMain = new PersonAddresses();
        personAddressToNotMain.setMainAddress(true);

        addresses.add(personAddressToMain);
        addresses.add(personAddressToNotMain);

        person.setPersonAddressesList(addresses);

        AddressRequestDto addressRequestDto = new AddressRequestDto();
        addressRequestDto.setMainAddress(true);

        addressService.ensuringOneMainsAddressAtTime(addressRequestDto, person, personAddressToMain);

        assertTrue(personAddressToMain.isMainAddress());
        assertFalse(personAddressToNotMain.isMainAddress());

        assertNotNull(person.getPersonAddressesList());

        PersonAddresses addressReturned = person.getPersonAddressesList().stream()
                .filter(a -> a == personAddressToMain)
                .findFirst()
                .orElse(null);
        assertNotNull(addressReturned);

        PersonAddresses nullAddress = new PersonAddresses();
        addressService.ensuringOneMainsAddressAtTime(addressRequestDto, person, nullAddress);
        assertNotNull(person.getPersonAddressesList());
    }


}