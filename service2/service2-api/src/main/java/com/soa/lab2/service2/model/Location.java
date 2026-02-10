package com.soa.lab2.service2.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "location")
@XmlAccessorType(XmlAccessType.FIELD)
public class Location implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @XmlElement
    private Double x;
    
    @XmlElement
    private Float y;
    
    @XmlElement
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
}

