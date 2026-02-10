package com.soa.lab2.service2.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "count")
@XmlAccessorType(XmlAccessType.FIELD)
public class CountResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @XmlElement
    private Integer count;
    
    public CountResponse() {}
    
    public CountResponse(Integer count) {
        this.count = count;
    }
    
    public Integer getCount() {
        return count;
    }
    
    public void setCount(Integer count) {
        this.count = count;
    }
}

