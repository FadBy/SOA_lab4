package com.soa.lab4.service1.proxy.controller;

import com.soa.lab4.service1.proxy.client.Service1BusClient;
import com.soa.lab4.service1.proxy.model.ErrorResponse;
import com.soa.lab4.service1.proxy.model.Person;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_XML_VALUE)
public class PersonProxyController {
    private final Service1BusClient busClient;

    public PersonProxyController(Service1BusClient busClient) {
        this.busClient = busClient;
    }

    @GetMapping("/persons")
    public ResponseEntity<?> getPersons(
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        try {
            return ResponseEntity.ok(busClient.getPersons(sort, filter, page, size));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping(value = "/persons", consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> createPerson(@Valid @RequestBody Person person) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(busClient.createPerson(person));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Invalid request: " + e.getMessage(), 400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/persons/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(busClient.getPersonById(id));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping(value = "/persons/{id}", consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> updatePerson(@PathVariable("id") Long id, @Valid @RequestBody Person person) {
        try {
            return ResponseEntity.ok(busClient.updatePerson(id, person));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Invalid request: " + e.getMessage(), 400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/persons/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable("id") Long id) {
        try {
            boolean deleted = busClient.deletePerson(id);
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
            return ResponseEntity.ok(busClient.getPersonWithMaxCoordinates());
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/persons/search")
    public ResponseEntity<?> searchPersonsByName(@RequestParam(name = "name") String name) {
        try {
            return ResponseEntity.ok(busClient.searchPersonsByName(name));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/persons/locations")
    public ResponseEntity<?> getAllLocations() {
        try {
            return ResponseEntity.ok(busClient.getAllLocations());
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
