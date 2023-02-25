package com.github.tlobato.PeopleManagerAPI.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.tlobato.PeopleManagerAPI.dto.request.PersonRequestDto;
import com.github.tlobato.PeopleManagerAPI.dto.response.PersonResponseDto;
import com.github.tlobato.PeopleManagerAPI.entities.Person;
import com.github.tlobato.PeopleManagerAPI.repository.PersonRepository;
import com.github.tlobato.PeopleManagerAPI.service.PersonService;
import java.time.LocalDate;
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
class PersonServiceImplTest {

    private final Long PERSON_ID = 1L;
    private final LocalDate BIRTHDATE = LocalDate.of(1988, 12, 13);

    private PersonRepository personRepository;

    private PersonService personService;

    private ModelMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new ModelMapper();
        personRepository = mock(PersonRepository.class);
        personService = new PersonServiceImpl(mapper, personRepository);
    }

    private PersonRequestDto buildPersonRequestDto() {
        return PersonRequestDto.builder()
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

    @Test
    @DisplayName("Must successfully receive a personRequestDto save a Person and return a personResponseDto")
    void createPersonTest() {

        PersonRequestDto personRequestDto = buildPersonRequestDto();

        Person personSaved = buildPerson();

        when(personRepository.save(any(Person.class))).thenReturn(personSaved);

        PersonResponseDto personResponseDto = personService.createPerson(personRequestDto);

        verify(personRepository).save(any(Person.class));

        assertEquals(personResponseDto.getName(), personSaved.getName());
        assertEquals(personResponseDto.getBirthDate(), personSaved.getBirthDate());
    }

    @Test
    @DisplayName("Must return a person with the same name and birthdate as saved in the database")
    void findPersonByIdTest() {

        Person savedPerson = buildPerson();
        savedPerson.setId(PERSON_ID);
        when(personRepository.findById(PERSON_ID)).thenReturn(Optional.of(savedPerson));

        PersonResponseDto personResponseDto = personService.findPersonById(PERSON_ID);

        assertEquals(personResponseDto.getName(), savedPerson.getName());
        assertEquals(personResponseDto.getBirthDate(), savedPerson.getBirthDate());
    }

    @Test
    @DisplayName("Must successfully return a list of personResponseDto.")
    void findAllPeopleTest() {
        List<Person> personList = Arrays.asList(
                buildPerson(), buildPerson(), buildPerson());

        when(personRepository.findAll()).thenReturn(personList);

        List<PersonResponseDto> personResponseDtoList = personList.stream()
                .map(person -> mapper.map(person, PersonResponseDto.class))
                .toList();

        personService.findAllPeople();

        verify(personRepository).findAll();

        assertEquals(personResponseDtoList.size(), personResponseDtoList.size());
        assertEquals(personResponseDtoList.get(0).getName(), personResponseDtoList.get(0).getName());
        assertEquals(personResponseDtoList.get(0).getBirthDate(), personResponseDtoList.get(0).getBirthDate());
        assertEquals(personResponseDtoList.get(1).getName(), personResponseDtoList.get(1).getName());
        assertEquals(personResponseDtoList.get(1).getBirthDate(), personResponseDtoList.get(1).getBirthDate());
        assertEquals(personResponseDtoList.get(2).getName(), personResponseDtoList.get(2).getName());
        assertEquals(personResponseDtoList.get(2).getBirthDate(), personResponseDtoList.get(2).getBirthDate());
    }

    @Test
    @DisplayName("Must successfully update a person.")
    void updatePersonTest() {

        PersonRequestDto personRequestDto = buildPersonRequestDto();

        Person savedPerson = buildPerson();
        savedPerson.setId(PERSON_ID);

        when(personRepository.findById(PERSON_ID)).thenReturn(Optional.of(savedPerson));
        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);

        PersonResponseDto updatedPersonResponseDto = personService.updatePerson(PERSON_ID, personRequestDto);

        verify(personRepository).save(any(Person.class));
        verify(personRepository).findById(PERSON_ID);

        assertEquals(updatedPersonResponseDto.getName(), personRequestDto.getName());
        assertEquals(updatedPersonResponseDto.getBirthDate(), personRequestDto.getBirthDate());
    }

    @Test
    @DisplayName("Must successfully set new attributes.")
    void setNewPersonAttributesTest() {

        Person person = new Person();
        PersonRequestDto personRequestDto = PersonRequestDto.builder()
                .name("Lobato")
                .birthDate(LocalDate.of(2008, 1, 5))
                .build();

        personService.setNewPersonAtributes(person, personRequestDto);

        assertEquals(person.getName(), personRequestDto.getName());
        assertEquals(person.getBirthDate(), personRequestDto.getBirthDate());
    }
}