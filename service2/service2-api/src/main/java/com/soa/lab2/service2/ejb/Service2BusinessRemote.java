package com.soa.lab2.service2.ejb;

import com.soa.lab2.service2.model.Color;
import com.soa.lab2.service2.model.Country;
import com.soa.lab2.service2.model.Person;
import jakarta.ejb.Remote;

@Remote
public interface Service2BusinessRemote {
    ServiceResponse getPersons(String sort, String filter, Integer page, Integer size);
    ServiceResponse createPerson(Person person);
    ServiceResponse getPersonById(Long id);
    ServiceResponse updatePerson(Long id, Person person);
    ServiceResponse deletePerson(Long id);
    ServiceResponse getPersonWithMaxCoordinates();
    ServiceResponse searchPersonsByName(String name);
    ServiceResponse getAllLocations();
    ServiceResponse getCountByNationality(Country nationality);
    ServiceResponse getCountByNationalityAndEyeColor(Country nationality, Color eyeColor);
}
