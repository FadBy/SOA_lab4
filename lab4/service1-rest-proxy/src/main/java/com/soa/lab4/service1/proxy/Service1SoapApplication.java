package com.soa.lab4.service1.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Service1SoapApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Service1SoapApplication.class, args);
    }
}
