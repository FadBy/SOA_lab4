package com.soa.lab4.service1.proxy.controller;

import com.soa.lab4.service1.proxy.client.Service1SoapClient;
import com.soa.lab4.service1.proxy.model.ErrorResponse;
import com.soa.lab4.service1.proxy.model.Location;
import com.soa.lab4.service1.proxy.model.LocationsWrapper;
import com.soa.lab4.service1.proxy.model.Person;
import com.soa.lab4.service1.proxy.model.PersonsWrapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_XML_VALUE)
@Validated
public class PersonProxyController {
    private final Service1SoapClient soapClient;

    public PersonProxyController(Service1SoapClient soapClient) {
        this.soapClient = soapClient;
    }

    @GetMapping("/persons")
    public ResponseEntity<?> getAllPersons(
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "filter", required = false) String filter,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        try {
            PersonsWrapper wrapper = soapClient.getPort().getPersons(sort, filter, page, size);
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
            Person created = soapClient.getPort().createPerson(person);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Invalid request: " + e.getMessage(), 400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/persons/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable("id") Long id) {
        try {
            Person person = soapClient.getPort().getPersonById(id);
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
            Person updated = soapClient.getPort().updatePerson(id, person);
            if (updated == null) {
                ErrorResponse error = new ErrorResponse("Person with id " + id + " not found", 404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Invalid request: " + e.getMessage(), 400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/persons/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable("id") Long id) {
        try {
            boolean deleted = soapClient.getPort().deletePerson(id);
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
            Person person = soapClient.getPort().getPersonWithMaxCoordinates();
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
            PersonsWrapper wrapper = soapClient.getPort().searchPersonsByName(name);
            return ResponseEntity.ok(wrapper);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/persons/locations")
    public ResponseEntity<?> getAllLocations() {
        try {
            LocationsWrapper wrapper = soapClient.getPort().getAllLocations();
            return ResponseEntity.ok(wrapper);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
