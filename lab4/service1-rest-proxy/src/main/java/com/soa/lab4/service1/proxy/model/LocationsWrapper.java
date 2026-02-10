package com.soa.lab4.service1.proxy.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Set;

@XmlRootElement(name = "locations")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocationsWrapper {
    @XmlElement(name = "location")
    private List<Location> locations;

    public LocationsWrapper() {}

    public LocationsWrapper(Set<Location> locations) {
        this.locations = new java.util.ArrayList<>(locations);
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
