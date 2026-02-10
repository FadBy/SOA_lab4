package com.soa.lab2.service2.config;

import com.soa.lab2.service2.resource.DemographyResource;
import com.soa.lab2.service2.resource.PersonResource;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class JaxRsApplication extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(PersonResource.class);
        classes.add(DemographyResource.class);
        classes.add(CorsFilter.class);
        return classes;
    }
}

