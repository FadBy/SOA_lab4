package com.soa.lab4.service1.proxy.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "count")
@XmlAccessorType(XmlAccessType.FIELD)
public class CountResponse {
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
