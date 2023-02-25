package com.github.tlobato.PeopleManagerAPI.service;

import com.github.tlobato.PeopleManagerAPI.dto.request.PersonRequestDto;
import com.github.tlobato.PeopleManagerAPI.dto.response.PersonResponseDto;
import com.github.tlobato.PeopleManagerAPI.entities.Person;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface PersonService {

    PersonResponseDto createPerson(PersonRequestDto personRequestDto);

    PersonResponseDto findPersonById(Long id);

    List<PersonResponseDto> findAllPeople();

    PersonResponseDto updatePerson(Long personId, PersonRequestDto personRequestDto);

    void setNewPersonAtributes(Person person, PersonRequestDto personRequestDto);
}