package com.github.tlobato.PeopleManagerAPI.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import com.github.tlobato.PeopleManagerAPI.entities.Person;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class PersonRepositoryTest {

    private final LocalDate BIRTHDATE = LocalDate.of(1988, 12, 13);

    @Autowired
    PersonRepository personRepository;

    private Person buildPerson() {
        return Person.builder()
                .name("Thyago")
                .birthDate(BIRTHDATE)
                .build();
    }

    @Test
    @DisplayName("Must save a person successfully.")
    void saveTest() {

        Person person = buildPerson();

        Person savedPerson = personRepository.save(person);

        assertThat(savedPerson.getId()).isNotNull();
        assertEquals(person.getName(), savedPerson.getName());
        assertEquals(person.getBirthDate(), savedPerson.getBirthDate());
    }

    @Test
    @DisplayName("Must find an employee by id successfully.")
    void findByIdTest() {
        Person person = buildPerson();
        personRepository.save(person);

        Optional<Person> foundEmployee = personRepository.findById(person.getId());

        assertThat(foundEmployee).isPresent();
    }
}