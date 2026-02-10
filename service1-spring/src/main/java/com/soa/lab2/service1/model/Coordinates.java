package com.soa.lab2.service1.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.persistence.Embeddable;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.validation.constraints.DecimalMin;

@Embeddable
@XmlRootElement(name = "coordinates")
@XmlAccessorType(XmlAccessType.FIELD)
public class Coordinates {
    
    @XmlElement
    @DecimalMin(value = "-195", message = "x must be >= -195")
    private Float x;
    
    @XmlElement
    private Integer y;
    
    public Coordinates() {}
    
    public Coordinates(Float x, Integer y) {
        this.x = x;
        this.y = y;
    }
    
    public Float getX() {
        return x;
    }
    
    public void setX(Float x) {
        this.x = x;
    }
    
    public Integer getY() {
        return y;
    }
    
    public void setY(Integer y) {
        this.y = y;
    }
}

