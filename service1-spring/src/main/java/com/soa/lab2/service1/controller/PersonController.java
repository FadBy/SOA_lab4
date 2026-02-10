package com.soa.lab2.service1.controller;

import com.soa.lab2.service1.model.*;
import com.soa.lab2.service1.service.PersonService;
import com.soa.lab2.service1.util.FilterParser;
import com.soa.lab2.service1.util.PageUtils;
import com.soa.lab2.service1.util.SortParser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_XML_VALUE)
@Validated
public class PersonController {
    
    @Autowired
    private PersonService personService;
    
    @GetMapping("/persons")
    public ResponseEntity<?> getAllPersons(
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "filter", required = false) String filter,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        
        try {
            List<Person> persons = personService.findAll();
            
            // Apply filter
            if (filter != null && !filter.trim().isEmpty()) {
                persons = FilterParser.applyFilter(persons, filter);
            }
            
            // Apply sort
            if (sort != null && !sort.trim().isEmpty()) {
                persons = SortParser.applySort(persons, sort);
            } else {
                persons = SortParser.applySort(persons, "id,asc");
            }
            
            // Apply pagination
            persons = PageUtils.paginate(persons, page, size);
            
            PersonsWrapper wrapper = new PersonsWrapper(persons);
            return ResponseEntity.ok(wrapper);
            
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), 400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PostMapping(value = "/persons", consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> createPerson(@Valid @RequestBody Person person) {
        try {
            Person created = personService.save(person);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Invalid request: " + e.getMessage(), 400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    @GetMapping("/persons/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable("id") Long id) {
        try {
            Person person = personService.findById(id);
            if (person == null) {
                ErrorResponse error = new ErrorResponse("Person with id " + id + " not found", 404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.ok(person);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PutMapping(value = "/persons/{id}", consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> updatePerson(@PathVariable("id") Long id, @Valid @RequestBody Person person) {
        try {
            Person existing = personService.findById(id);
            if (existing == null) {
                ErrorResponse error = new ErrorResponse("Person with id " + id + " not found", 404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            person.setId(id);
            person.setCreationDate(existing.getCreationDate());
            Person updated = personService.save(person);
            return ResponseEntity.ok(updated);
            
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Invalid request: " + e.getMessage(), 400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    @DeleteMapping("/persons/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable("id") Long id) {
        try {
            boolean deleted = personService.deleteById(id);
            if (!deleted) {
                ErrorResponse error = new ErrorResponse("Person with id " + id + " not found", 404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/persons/max-coordinates")
    public ResponseEntity<?> getPersonWithMaxCoordinates() {
        try {
            Person person = personService.findMaxCoordinates();
            if (person == null) {
                ErrorResponse error = new ErrorResponse("Person not found", 404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.ok(person);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/persons/search")
    public ResponseEntity<?> searchPersonsByName(@RequestParam(name = "name") String name) {
        try {
            List<Person> persons = personService.findByNameSubstring(name);
            PersonsWrapper wrapper = new PersonsWrapper(persons);
            return ResponseEntity.ok(wrapper);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/persons/locations")
    public ResponseEntity<?> getAllLocations() {
        try {
            Set<Location> locations = personService.findAllUniqueLocations();
            LocationsWrapper wrapper = new LocationsWrapper(locations);
            return ResponseEntity.ok(wrapper);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

