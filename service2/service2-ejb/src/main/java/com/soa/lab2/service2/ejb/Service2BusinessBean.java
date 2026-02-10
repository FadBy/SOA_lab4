package com.soa.lab2.service2.ejb;

import com.soa.lab2.service2.client.Service1Client;
import com.soa.lab2.service2.model.Color;
import com.soa.lab2.service2.model.Country;
import com.soa.lab2.service2.model.Person;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.core.Response;

@Stateless
public class Service2BusinessBean implements Service2BusinessRemote {
    
    private Service1Client service1Client;
    
    @PostConstruct
    public void init() {
        this.service1Client = new Service1Client();
    }
    
    @Override
    public ServiceResponse getPersons(String sort, String filter, Integer page, Integer size) {
        return toServiceResponse(service1Client.getPersons(sort, filter, page, size));
    }
    
    @Override
    public ServiceResponse createPerson(Person person) {
        return toServiceResponse(service1Client.createPerson(person));
    }
    
    @Override
    public ServiceResponse getPersonById(Long id) {
        return toServiceResponse(service1Client.getPersonById(id));
    }
    
    @Override
    public ServiceResponse updatePerson(Long id, Person person) {
        return toServiceResponse(service1Client.updatePerson(id, person));
    }
    
    @Override
    public ServiceResponse deletePerson(Long id) {
        return toServiceResponse(service1Client.deletePerson(id));
    }
    
    @Override
    public ServiceResponse getPersonWithMaxCoordinates() {
        return toServiceResponse(service1Client.getPersonWithMaxCoordinates());
    }
    
    @Override
    public ServiceResponse searchPersonsByName(String name) {
        return toServiceResponse(service1Client.searchPersonsByName(name));
    }
    
    @Override
    public ServiceResponse getAllLocations() {
        return toServiceResponse(service1Client.getAllLocations());
    }
    
    @Override
    public ServiceResponse getCountByNationality(Country nationality) {
        return toServiceResponse(service1Client.getCountByNationality(nationality));
    }
    
    @Override
    public ServiceResponse getCountByNationalityAndEyeColor(Country nationality, Color eyeColor) {
        return toServiceResponse(service1Client.getCountByNationalityAndEyeColor(nationality, eyeColor));
    }
    
    private ServiceResponse toServiceResponse(Response response) {
        if (response == null) {
            return new ServiceResponse(500, null);
        }
        String entity = response.hasEntity() ? response.readEntity(String.class) : null;
        return new ServiceResponse(response.getStatus(), entity);
    }
}
