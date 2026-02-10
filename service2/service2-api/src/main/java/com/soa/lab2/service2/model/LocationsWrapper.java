package com.soa.lab2.service2.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "locations")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocationsWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @XmlElement(name = "location")
    private List<Location> locations;
    
    public LocationsWrapper() {}
    
    public LocationsWrapper(List<Location> locations) {
        this.locations = locations;
    }
    
    public List<Location> getLocations() {
        return locations;
    }
    
    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}

