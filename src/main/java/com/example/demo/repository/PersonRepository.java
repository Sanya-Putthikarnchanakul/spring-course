package com.example.demo.repository;

import com.example.demo.entity.Person;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface PersonRepository extends Repository<Person,Long> {

    // when using 'Repository<Entity, ID>' we need to implement save()
    Person save(Person person);

    Optional<Person> findById(long id);

}
