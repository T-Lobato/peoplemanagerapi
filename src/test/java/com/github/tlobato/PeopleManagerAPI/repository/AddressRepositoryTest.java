package com.github.tlobato.PeopleManagerAPI.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

import com.github.tlobato.PeopleManagerAPI.dto.request.AddressRequestDto;
import com.github.tlobato.PeopleManagerAPI.dto.response.AddressResponseDto;
import com.github.tlobato.PeopleManagerAPI.entities.Person;
import com.github.tlobato.PeopleManagerAPI.entities.PersonAddresses;
import com.github.tlobato.PeopleManagerAPI.integrations.ViaCepApi;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class AddressRepositoryTest {

    private final LocalDate BIRTHDATE = LocalDate.of(1988, 12, 13);

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    PersonRepository personRepository;

    @Mock
    ViaCepApi viaCepApi;

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
    @Transactional
    @DisplayName("Must save a person successfully.")
    void saveTest() {

        Person person = buildPerson();
        personRepository.save(person);

        AddressRequestDto addressRequestDto = buildAddressRequestDto();

        AddressRequestDto addressAux = AddressRequestDto.builder()
                .logradouro("Rua Ana Augusto")
                .localidade("Sorocaba")
                .build();

        when(viaCepApi.getCompleteAddress(addressRequestDto.getCep())).thenReturn(addressAux);

        viaCepApi.getCompleteAddress(addressRequestDto.getCep());

        PersonAddresses savedAddress = PersonAddresses.builder()
                .street(addressAux.getLogradouro())
                .city(addressAux.getLocalidade())
                .zipCode(addressRequestDto.getCep())
                .houseNumber(addressRequestDto.getHouseNumber())
                .mainAddress(addressRequestDto.isMainAddress())
                .person(person)
                .build();

        addressRepository.save(savedAddress);

        assertEquals(savedAddress.getStreet(), addressAux.getLogradouro());
        assertEquals(savedAddress.getCity(), addressAux.getLocalidade());
        assertEquals(savedAddress.getZipCode(), addressRequestDto.getCep());
        assertEquals(savedAddress.getHouseNumber(), addressRequestDto.getHouseNumber());
        assertEquals(savedAddress.isMainAddress(), addressRequestDto.isMainAddress());
    }

    @Test
    @Transactional
    @DisplayName("Must find an address by id.")
    void findByIdTest() {

        PersonAddresses personAddresses = buildPersonAddresses();
        Person person = buildPerson();

        personRepository.save(person);
        personAddresses.setPerson(person);
        addressRepository.save(personAddresses);

        Optional<PersonAddresses> savedAddress = addressRepository.findById(personAddresses.getId());

        assertThat(savedAddress).isPresent();
        assertEquals(savedAddress.get().getStreet(), personAddresses.getStreet());
        assertEquals(savedAddress.get().getCity(), personAddresses.getCity());
        assertEquals(savedAddress.get().getZipCode(), personAddresses.getZipCode());
        assertEquals(savedAddress.get().getHouseNumber(), personAddresses.getHouseNumber());
        assertEquals(savedAddress.get().isMainAddress(), personAddresses.isMainAddress());
    }

    @Test
    @Transactional
    @DisplayName("Must find all addresses by person id.")
    void findAllByPersonIdTest() {

        PersonAddresses personAddresses1 = buildPersonAddresses();
        PersonAddresses personAddresses2 = buildPersonAddresses();
        Person person = buildPerson();

        personRepository.save(person);
        personAddresses1.setPerson(person);
        personAddresses2.setPerson(person);
        addressRepository.saveAll(Arrays.asList(personAddresses1, personAddresses2));

        List<PersonAddresses> savedAddresses = addressRepository.findAllByPersonId(person.getId());

        assertThat(savedAddresses).isNotNull();
        assertEquals(2, savedAddresses.size());
        assertIterableEquals(savedAddresses, Arrays.asList(personAddresses1, personAddresses2));
    }
}