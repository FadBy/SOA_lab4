package com.soa.lab2.service2.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "coordinates")
@XmlAccessorType(XmlAccessType.FIELD)
public class Coordinates implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @XmlElement
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

