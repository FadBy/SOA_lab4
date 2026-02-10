package com.soa.lab4.service1.proxy.controller;

import com.soa.lab4.service1.proxy.client.Service1SoapClient;
import com.soa.lab4.service1.proxy.model.Color;
import com.soa.lab4.service1.proxy.model.CountResponse;
import com.soa.lab4.service1.proxy.model.Country;
import com.soa.lab4.service1.proxy.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_XML_VALUE)
public class DemographyProxyController {
    private final Service1SoapClient soapClient;

    public DemographyProxyController(Service1SoapClient soapClient) {
        this.soapClient = soapClient;
    }

    @GetMapping("/demography/nationality/{nationality}/hair-color")
    public ResponseEntity<?> getCountByNationality(@PathVariable("nationality") Country nationality) {
        try {
            CountResponse response = soapClient.getPort().getCountByNationality(nationality);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/demography/nationality/{nationality}/eye-color/{eye-color}")
    public ResponseEntity<?> getCountByNationalityAndEyeColor(
            @PathVariable("nationality") Country nationality,
            @PathVariable("eye-color") Color eyeColor
    ) {
        try {
            CountResponse response = soapClient.getPort().getCountByNationalityAndEyeColor(nationality, eyeColor);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
