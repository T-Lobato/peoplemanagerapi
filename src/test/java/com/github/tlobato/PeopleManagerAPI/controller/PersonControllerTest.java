package com.github.tlobato.PeopleManagerAPI.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.github.tlobato.PeopleManagerAPI.dto.request.AddressRequestDto;
import com.github.tlobato.PeopleManagerAPI.dto.request.PersonRequestDto;
import com.github.tlobato.PeopleManagerAPI.dto.response.AddressResponseDto;
import com.github.tlobato.PeopleManagerAPI.dto.response.PersonResponseDto;
import com.github.tlobato.PeopleManagerAPI.exception.InvalidInputException;
import com.github.tlobato.PeopleManagerAPI.exception.NotFoundException;
import com.github.tlobato.PeopleManagerAPI.integrations.ViaCepApi;
import com.github.tlobato.PeopleManagerAPI.service.AddressService;
import com.github.tlobato.PeopleManagerAPI.service.PersonService;
import com.github.tlobato.PeopleManagerAPI.shared.enums.ErrorCode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PersonControllerTest {

    private final Long PERSON_ID = 1L;
    private final Long ADDRESS_ID = 1L;
    private final LocalDate BIRTHDATE = LocalDate.of(1988, 12, 13);

    private PersonController personController;

    private PersonService personService;

    private AddressService addressService;

    private ViaCepApi viaCepApi;

    private ModelMapper mapper;

    @BeforeEach
    void setup() {
        personService = mock(PersonService.class);
        addressService = mock(AddressService.class);
        viaCepApi = mock(ViaCepApi.class);
        mapper = new ModelMapper();
        personController = new PersonController(personService, addressService, mapper);
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

    private AddressRequestDto buildAddressRequestDto() {
        return AddressRequestDto.builder()
                .logradouro("Rua Ana Augusto")
                .localidade("Sorocaba")
                .cep("18040040")
                .houseNumber("600")
                .mainAddress(false)
                .build();
    }

    private AddressResponseDto buildAddressResponseDto() {
        return AddressResponseDto.builder()
                .id(PERSON_ID)
                .street("Rua Ana Augusto")
                .city("Sorocaba")
                .zipCode("18040040")
                .houseNumber("600")
                .mainAddress(false)
                .build();
    }

    @Test
    @DisplayName("Must successfully receive a personRequestDto and return a personResponseDto.")
    void createPersonTest() {

        PersonRequestDto personRequestDto = buildPersonRequestDto();

        PersonResponseDto personResponseDto = mapper.map(personRequestDto, PersonResponseDto.class);

        when(personService.createPerson(personRequestDto)).thenReturn(personResponseDto);

        PersonResponseDto actualResponse = personController.createPerson(personRequestDto);

        verify(personService).createPerson(personRequestDto);

        assertEquals(personResponseDto, actualResponse);
        assertEquals(personResponseDto.getName(), actualResponse.getName());
        assertEquals(personResponseDto.getBirthDate(), actualResponse.getBirthDate());
    }

     @Test
    @DisplayName("Must successfully receive an id and return a personResponseDto.")
    void findPersonByIdTest() {

        PersonResponseDto personResponseDto = buildPersonResponseDto();

        when(personService.findPersonById(PERSON_ID)).thenReturn(personResponseDto);

        PersonResponseDto actualResponse = personController.findPersonById(PERSON_ID);

        verify(personService).findPersonById(PERSON_ID);

        assertEquals(personResponseDto, actualResponse);
        assertEquals(personResponseDto.getId(), actualResponse.getId());
        assertEquals(personResponseDto.getName(), actualResponse.getName());
        assertEquals(personResponseDto.getBirthDate(), actualResponse.getBirthDate());
    }

    @Test
    @DisplayName("Must throw a NotFoundException when id does not exist")
    void idNotFoundTest() {
        when(personService.findPersonById(PERSON_ID)).thenThrow(new NotFoundException(ErrorCode.EC001.getMessage(), ErrorCode.EC001.getCode()));

        assertThrows(NotFoundException.class, () -> personController.findPersonById(PERSON_ID));

        verify(personService).findPersonById(PERSON_ID);

    }

    @Test
    @DisplayName("Must throw a InvalidInputException when zip code entered is invalid")
    void invalidRequestTest() {

        when(addressService.create(any(Long.class), any(AddressRequestDto.class))).thenThrow(new InvalidInputException(ErrorCode.EC101.getMessage(), ErrorCode.EC101.getCode()));

        assertThrows(InvalidInputException.class, () -> personController.createAddress(1L, buildAddressRequestDto()));

        verify(addressService).create(any(), any());
    }

    @Test
    @DisplayName("Must successfully return a list of personReponseDto.")
    void findAllPeopleTest() {

        List<PersonResponseDto> personResponseDtoList = Arrays.asList(
                buildPersonResponseDto(), buildPersonResponseDto(), buildPersonResponseDto());

        when(personService.findAllPeople()).thenReturn(personResponseDtoList);

        List<PersonResponseDto> personResponseDtoActualList = personController.findAllPeople();

        verify(personService).findAllPeople();

        assertIterableEquals(personResponseDtoList, personResponseDtoActualList);
    }

    @Test
    @DisplayName("Must successfully update a person.")
    void updatePersonTest() {

        PersonRequestDto personRequestDto = buildPersonRequestDto();

        PersonResponseDto personResponseDto = mapper.map(personRequestDto, PersonResponseDto.class);

        when(personService.updatePerson(PERSON_ID, personRequestDto)).thenReturn(personResponseDto);

        PersonResponseDto actualResponse = personController.updatePerson(PERSON_ID, personRequestDto);

        verify(personService).updatePerson(PERSON_ID, personRequestDto);

        assertEquals(personResponseDto, actualResponse);
        assertEquals(personResponseDto.getName(), actualResponse.getName());
        assertEquals(personResponseDto.getBirthDate(), actualResponse.getBirthDate());
    }

    @Test
    @DisplayName("Must successfully receive an addressRequestDto and return an addressResponseDto.")
    void createAddressTest() {

        AddressRequestDto addressRequestDto = buildAddressRequestDto();

        AddressResponseDto addressResponseDto = mapper.map(addressRequestDto, AddressResponseDto.class);

        when(addressService.create(PERSON_ID, addressRequestDto)).thenReturn(addressResponseDto);

        AddressResponseDto actualResponse = personController.createAddress(PERSON_ID, addressRequestDto);

        verify(addressService).create(PERSON_ID, addressRequestDto);

        assertEquals(addressResponseDto, actualResponse);
        assertEquals(addressResponseDto.getCity(), actualResponse.getCity());
        assertEquals(addressResponseDto.getStreet(), actualResponse.getStreet());
        assertEquals(addressResponseDto.getZipCode(), actualResponse.getZipCode());
        assertEquals(addressResponseDto.getHouseNumber(), actualResponse.getHouseNumber());
        assertEquals(addressResponseDto.isMainAddress(), actualResponse.isMainAddress());
    }

    @Test
    @DisplayName("Must successfully return a list of AddressesReponseDto from a person.")
    void getAddressesTest() {

        List<AddressResponseDto> addressResponseDtoList = Arrays.asList(
                buildAddressResponseDto(), buildAddressResponseDto(), buildAddressResponseDto());

        when(addressService.getAddresses(PERSON_ID)).thenReturn(addressResponseDtoList);

        List<AddressResponseDto> addressResponseDtoActualList = personController.getAddresses(PERSON_ID);

        verify(addressService).getAddresses(PERSON_ID);

        assertIterableEquals(addressResponseDtoList, addressResponseDtoActualList);
    }

    @Test
    @DisplayName("Must successfully update an address to main address.")
    void updateMainAddressTest() {

        AddressRequestDto addressRequestDto = buildAddressRequestDto();

        AddressResponseDto addressResponseDto = mapper.map(addressRequestDto, AddressResponseDto.class);
        addressResponseDto.setId(ADDRESS_ID);
        addressResponseDto.setMainAddress(true);

        when(addressService.updateMainAddress(PERSON_ID, ADDRESS_ID)).thenReturn(addressResponseDto);

        AddressResponseDto updatedAddressResponseDto = personController.updateMainAddress(PERSON_ID, addressResponseDto.getId());

        verify(addressService).updateMainAddress(PERSON_ID, ADDRESS_ID);

        assertTrue(updatedAddressResponseDto.isMainAddress());
    }
}