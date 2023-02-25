package com.github.tlobato.PeopleManagerAPI.service.impl;

import com.github.tlobato.PeopleManagerAPI.dto.request.PersonRequestDto;
import com.github.tlobato.PeopleManagerAPI.dto.response.PersonResponseDto;
import com.github.tlobato.PeopleManagerAPI.entities.Person;
import com.github.tlobato.PeopleManagerAPI.exception.NotFoundException;
import com.github.tlobato.PeopleManagerAPI.repository.PersonRepository;
import com.github.tlobato.PeopleManagerAPI.service.PersonService;
import com.github.tlobato.PeopleManagerAPI.shared.enums.ErrorCode;
import java.util.List;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final ModelMapper mapper;

    private final PersonRepository personRepository;

    @Override
    @Transactional
    public PersonResponseDto createPerson(PersonRequestDto personRequestDto) {
        Person person = personRepository.save(mapper.map(personRequestDto, Person.class));
        return mapper.map(person, PersonResponseDto.class);
    }

    @Override
    public PersonResponseDto findPersonById(Long id) {
        return mapper.map(personRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.EC001.getMessage(), ErrorCode.EC001.getCode())), PersonResponseDto.class);
    }

    @Override
    public List<PersonResponseDto> findAllPeople() {
        return personRepository.findAll()
                .stream()
                .map(person -> mapper.map(person, PersonResponseDto.class)).toList();
    }

    @Override
    @Transactional
    public PersonResponseDto updatePerson(Long personId, PersonRequestDto personRequestDto) {
        Person person = mapper.map(findPersonById(personId), Person.class);
        setNewPersonAtributes(person, personRequestDto);
        return mapper.map(personRepository.save(person), PersonResponseDto.class);
    }

    @Override
    public void setNewPersonAtributes(Person person, PersonRequestDto personRequestDto) {
        person.setName(personRequestDto.getName());
        person.setBirthDate(personRequestDto.getBirthDate());
    }
}