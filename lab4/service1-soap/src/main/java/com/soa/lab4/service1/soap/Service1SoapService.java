package com.soa.lab4.service1.soap;

import com.soa.lab4.service1.soap.model.Color;
import com.soa.lab4.service1.soap.model.CountResponse;
import com.soa.lab4.service1.soap.model.Country;
import com.soa.lab4.service1.soap.model.Location;
import com.soa.lab4.service1.soap.model.LocationsWrapper;
import com.soa.lab4.service1.soap.model.Person;
import com.soa.lab4.service1.soap.model.PersonsWrapper;
import com.soa.lab4.service1.soap.repository.PersonRepository;
import com.soa.lab4.service1.soap.util.FilterParser;
import com.soa.lab4.service1.soap.util.PageUtils;
import com.soa.lab4.service1.soap.util.SortParser;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.List;
import java.util.Set;

@WebService(serviceName = "Service1SoapService", targetNamespace = "http://soap.service1.lab4.soa.com/")
public class Service1SoapService {
    private final PersonRepository repository = new PersonRepository();

    @WebMethod
    public PersonsWrapper getPersons(
            @WebParam(name = "sort") String sort,
            @WebParam(name = "filter") String filter,
            @WebParam(name = "page") Integer page,
            @WebParam(name = "size") Integer size
    ) {
        int resolvedPage = page == null ? 0 : page;
        int resolvedSize = size == null ? 10 : size;
        List<Person> persons = repository.findAll();
        persons = FilterParser.applyFilter(persons, filter);
        persons = SortParser.applySort(persons, sort == null || sort.trim().isEmpty() ? "id,asc" : sort);
        persons = PageUtils.paginate(persons, resolvedPage, resolvedSize);
        return new PersonsWrapper(persons);
    }

    @WebMethod
    public Person createPerson(@WebParam(name = "person") Person person) {
        return repository.save(person);
    }

    @WebMethod
    public Person getPersonById(@WebParam(name = "id") Long id) {
        return repository.findById(id).orElse(null);
    }

    @WebMethod
    public Person updatePerson(
            @WebParam(name = "id") Long id,
            @WebParam(name = "person") Person person
    ) {
        Person existing = repository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        person.setId(id);
        person.setCreationDate(existing.getCreationDate());
        return repository.save(person);
    }

    @WebMethod
    public boolean deletePerson(@WebParam(name = "id") Long id) {
        return repository.deleteById(id);
    }

    @WebMethod
    public Person getPersonWithMaxCoordinates() {
        return repository.findMaxCoordinates();
    }

    @WebMethod
    public PersonsWrapper searchPersonsByName(@WebParam(name = "name") String name) {
        List<Person> persons = repository.findByNameSubstring(name);
        return new PersonsWrapper(persons);
    }

    @WebMethod
    public LocationsWrapper getAllLocations() {
        Set<Location> locations = repository.findAllUniqueLocations();
        return new LocationsWrapper(locations);
    }

    @WebMethod
    public CountResponse getCountByNationality(@WebParam(name = "nationality") Country nationality) {
        long count = repository.countByNationality(nationality);
        return new CountResponse((int) count);
    }

    @WebMethod
    public CountResponse getCountByNationalityAndEyeColor(
            @WebParam(name = "nationality") Country nationality,
            @WebParam(name = "eyeColor") Color eyeColor
    ) {
        long count = repository.countByNationalityAndEyeColor(nationality, eyeColor);
        return new CountResponse((int) count);
    }
}
