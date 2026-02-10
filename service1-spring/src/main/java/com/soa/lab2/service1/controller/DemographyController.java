package com.soa.lab2.service1.controller;

import com.soa.lab2.service1.model.*;
import com.soa.lab2.service1.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_XML_VALUE)
public class DemographyController {
    
    @Autowired
    private PersonService personService;
    
    @GetMapping("/demography/nationality/{nationality}/hair-color")
    public ResponseEntity<?> getCountByNationality(@PathVariable("nationality") Country nationality) {
        try {
            long count = personService.countByNationality(nationality);
            CountResponse response = new CountResponse((int) count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/demography/nationality/{nationality}/eye-color/{eye-color}")
    public ResponseEntity<?> getCountByNationalityAndEyeColor(
            @PathVariable("nationality") Country nationality,
            @PathVariable("eye-color") Color eyeColor) {
        try {
            long count = personService.countByNationalityAndEyeColor(nationality, eyeColor);
            CountResponse response = new CountResponse((int) count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

