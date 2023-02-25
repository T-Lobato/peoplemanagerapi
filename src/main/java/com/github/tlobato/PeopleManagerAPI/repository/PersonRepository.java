package com.github.tlobato.PeopleManagerAPI.repository;

import com.github.tlobato.PeopleManagerAPI.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
