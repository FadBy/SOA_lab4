package com.soa.lab2.service2.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "persons")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonsWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @XmlElement(name = "person")
    private List<Person> persons;
    
    public PersonsWrapper() {}
    
    public PersonsWrapper(List<Person> persons) {
        this.persons = persons;
    }
    
    public List<Person> getPersons() {
        return persons;
    }
    
    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}

