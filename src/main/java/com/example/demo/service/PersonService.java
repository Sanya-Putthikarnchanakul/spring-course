package com.example.demo.service;

import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class PersonService {

    private static final Logger log = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    private PersonRepository personRepository;

    public Long createPerson(String name) {
        var addedPerson = new Person();
        addedPerson.setName(name);

        addedPerson = personRepository.save(addedPerson);
        log.debug("[PersonService.createPerson] => {}", addedPerson.getId());

        return addedPerson.getId();
    }

    public HashMap<String, Object> getPersonById(Long id){
        Optional<Person> optPerson = personRepository.findById(id);

        if (optPerson.isEmpty()){
            log.warn("[PersonService.getPersonById] => empty via id={}", id);
            return null;
        }

        Person person = optPerson.get();

        var response = new HashMap<String, Object>();
        response.put("id", person.getId());
        response.put("fullname", person.getName());

        return response;
    }

}
