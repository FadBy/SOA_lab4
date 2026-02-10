package com.soa.lab2.service1.service;

import com.soa.lab2.service1.model.*;
import com.soa.lab2.service1.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonService {
    
    @Autowired
    private PersonRepository personRepository;
    
    public List<Person> findAll() {
        return personRepository.findAll();
    }
    
    public Person findById(Long id) {
        return personRepository.findById(id).orElse(null);
    }
    
    public Person save(Person person) {
        if (person.getCreationDate() == null) {
            person.setCreationDate(LocalDate.now());
        }
        return personRepository.save(person);
    }
    
    public boolean deleteById(Long id) {
        if (!personRepository.existsById(id)) {
            return false;
        }
        personRepository.deleteById(id);
        return true;
    }
    
    public Person findMaxCoordinates() {
        List<Person> result = personRepository.findMaxCoordinates(PageRequest.of(0, 1));
        return result.isEmpty() ? null : result.get(0);
    }
    
    public List<Person> findByNameSubstring(String name) {
        return personRepository.findByNameContaining(name);
    }
    
    public Set<Location> findAllUniqueLocations() {
        return personRepository.findAll().stream()
                .map(Person::getLocation)
                .collect(Collectors.toSet());
    }
    
    public long countByNationalityAndEyeColor(Country nationality, Color eyeColor) {
        return personRepository.countByNationalityAndEyeColor(nationality, eyeColor);
    }
    
    public long countByNationality(Country nationality) {
        return personRepository.countByNationality(nationality);
    }
}

