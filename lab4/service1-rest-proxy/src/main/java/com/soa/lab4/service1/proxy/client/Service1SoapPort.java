package com.soa.lab4.service1.proxy.client;

import com.soa.lab4.service1.proxy.model.Color;
import com.soa.lab4.service1.proxy.model.CountResponse;
import com.soa.lab4.service1.proxy.model.Country;
import com.soa.lab4.service1.proxy.model.LocationsWrapper;
import com.soa.lab4.service1.proxy.model.Person;
import com.soa.lab4.service1.proxy.model.PersonsWrapper;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(name = "Service1SoapService", targetNamespace = "http://soap.service1.lab4.soa.com/")
public interface Service1SoapPort {
    @WebMethod
    PersonsWrapper getPersons(
            @WebParam(name = "sort") String sort,
            @WebParam(name = "filter") String filter,
            @WebParam(name = "page") Integer page,
            @WebParam(name = "size") Integer size
    );

    @WebMethod
    Person createPerson(@WebParam(name = "person") Person person);

    @WebMethod
    Person getPersonById(@WebParam(name = "id") Long id);

    @WebMethod
    Person updatePerson(
            @WebParam(name = "id") Long id,
            @WebParam(name = "person") Person person
    );

    @WebMethod
    boolean deletePerson(@WebParam(name = "id") Long id);

    @WebMethod
    Person getPersonWithMaxCoordinates();

    @WebMethod
    PersonsWrapper searchPersonsByName(@WebParam(name = "name") String name);

    @WebMethod
    LocationsWrapper getAllLocations();

    @WebMethod
    CountResponse getCountByNationality(@WebParam(name = "nationality") Country nationality);

    @WebMethod
    CountResponse getCountByNationalityAndEyeColor(
            @WebParam(name = "nationality") Country nationality,
            @WebParam(name = "eyeColor") Color eyeColor
    );
}
