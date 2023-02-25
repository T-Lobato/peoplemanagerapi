package com.github.tlobato.PeopleManagerAPI.repository;

import com.github.tlobato.PeopleManagerAPI.entities.PersonAddresses;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<PersonAddresses, Long> {
    List<PersonAddresses> findAllByPersonId(Long personId);
}