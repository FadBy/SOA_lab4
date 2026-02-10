package com.soa.lab2.service1.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.persistence.Embeddable;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.validation.constraints.Size;

@Embeddable
@XmlRootElement(name = "location")
@XmlAccessorType(XmlAccessType.FIELD)
public class Location {
    
    @XmlElement
    private Double x;
    
    @XmlElement
    private Float y;
    
    @XmlElement
    @Size(max = 554, message = "name length must be <= 554")
    private String name;
    
    public Location() {}
    
    public Location(Double x, Float y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }
    
    public Double getX() {
        return x;
    }
    
    public void setX(Double x) {
        this.x = x;
    }
    
    public Float getY() {
        return y;
    }
    
    public void setY(Float y) {
        this.y = y;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        if (x != null ? !x.equals(location.x) : location.x != null) return false;
        if (y != null ? !y.equals(location.y) : location.y != null) return false;
        return name != null ? name.equals(location.name) : location.name == null;
    }
    
    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}

