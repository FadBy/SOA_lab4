package com.soa.lab2.service1.util;

import com.soa.lab2.service1.model.Person;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterParser {
    
    private static final Pattern FILTER_PATTERN = Pattern.compile(
        "^(\\w+)\\s*(>|<|>=|<=|==|!=)\\s*([-\\d.]+)$"
    );
    
    public static Predicate<Person> parseFilter(String filter) {
        if (filter == null || filter.trim().isEmpty()) {
            return p -> true;
        }
        
        Matcher matcher = FILTER_PATTERN.matcher(filter.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid filter format: " + filter);
        }
        
        String field = matcher.group(1);
        String operator = matcher.group(2);
        String value = matcher.group(3);
        
        switch (field.toLowerCase()) {
            case "height":
                return parseHeightFilter(operator, Float.parseFloat(value));
            case "id":
                return parseIdFilter(operator, Long.parseLong(value));
            default:
                throw new IllegalArgumentException("Unknown filter field: " + field);
        }
    }
    
    private static Predicate<Person> parseHeightFilter(String operator, Float value) {
        switch (operator) {
            case ">": return p -> p.getHeight() > value;
            case "<": return p -> p.getHeight() < value;
            case ">=": return p -> p.getHeight() >= value;
            case "<=": return p -> p.getHeight() <= value;
            case "==": return p -> p.getHeight().equals(value);
            case "!=": return p -> !p.getHeight().equals(value);
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }
    
    private static Predicate<Person> parseIdFilter(String operator, Long value) {
        switch (operator) {
            case ">": return p -> p.getId() > value;
            case "<": return p -> p.getId() < value;
            case ">=": return p -> p.getId() >= value;
            case "<=": return p -> p.getId() <= value;
            case "==": return p -> p.getId().equals(value);
            case "!=": return p -> !p.getId().equals(value);
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }
    
    public static List<Person> applyFilter(List<Person> persons, String filter) {
        if (filter == null || filter.trim().isEmpty()) {
            return persons;
        }
        Predicate<Person> predicate = parseFilter(filter);
        return persons.stream()
                .filter(predicate)
                .collect(java.util.stream.Collectors.toList());
    }
}

