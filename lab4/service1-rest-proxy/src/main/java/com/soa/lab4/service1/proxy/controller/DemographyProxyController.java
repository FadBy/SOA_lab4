package com.soa.lab4.service1.proxy.controller;

import com.soa.lab4.service1.proxy.client.Service1BusClient;
import com.soa.lab4.service1.proxy.model.Color;
import com.soa.lab4.service1.proxy.model.Country;
import com.soa.lab4.service1.proxy.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/demography", produces = MediaType.APPLICATION_XML_VALUE)
public class DemographyProxyController {
    private final Service1BusClient busClient;

    public DemographyProxyController(Service1BusClient busClient) {
        this.busClient = busClient;
    }

    @GetMapping("/nationality/{nationality}/hair-color")
    public ResponseEntity<?> getCountByNationality(@PathVariable("nationality") String nationality) {
        try {
            Country country = Country.valueOf(nationality);
            return ResponseEntity.ok(busClient.getCountByNationality(country));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Invalid nationality", 400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/nationality/{nationality}/eye-color/{eyeColor}")
    public ResponseEntity<?> getCountByNationalityAndEyeColor(
            @PathVariable("nationality") String nationality,
            @PathVariable("eyeColor") String eyeColor
    ) {
        try {
            Country country = Country.valueOf(nationality);
            Color color = Color.valueOf(eyeColor);
            return ResponseEntity.ok(busClient.getCountByNationalityAndEyeColor(country, color));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Invalid parameters", 400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
