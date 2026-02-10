package com.soa.lab2.service1.util;

import com.soa.lab2.service1.model.Person;
import java.util.Comparator;
import java.util.List;

public class SortParser {
    
    public static Comparator<Person> parseSort(String sort) {
        if (sort == null || sort.trim().isEmpty()) {
            return Comparator.comparing(Person::getId);
        }
        
        String[] parts = sort.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid sort format. Expected: field,asc|desc");
        }
        
        String field = parts[0].trim().toLowerCase();
        String direction = parts[1].trim().toLowerCase();
        
        Comparator<Person> comparator;
        
        switch (field) {
            case "id":
                comparator = Comparator.comparing(Person::getId);
                break;
            case "name":
                comparator = Comparator.comparing(Person::getName);
                break;
            case "height":
                comparator = Comparator.comparing(Person::getHeight);
                break;
            case "creationdate":
            case "creation_date":
                comparator = Comparator.comparing(Person::getCreationDate, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            default:
                throw new IllegalArgumentException("Unknown sort field: " + field);
        }
        
        if ("desc".equals(direction)) {
            comparator = comparator.reversed();
        } else if (!"asc".equals(direction)) {
            throw new IllegalArgumentException("Invalid sort direction. Expected: asc|desc");
        }
        
        return comparator;
    }
    
    public static List<Person> applySort(List<Person> persons, String sort) {
        if (sort == null || sort.trim().isEmpty()) {
            return persons;
        }
        Comparator<Person> comparator = parseSort(sort);
        return persons.stream()
                .sorted(comparator)
                .collect(java.util.stream.Collectors.toList());
    }
}

