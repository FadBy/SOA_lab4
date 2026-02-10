package com.soa.lab2.service2.ejb;

import java.io.Serializable;

public class ServiceResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int status;
    private String entity;
    
    public ServiceResponse() {
    }
    
    public ServiceResponse(int status, String entity) {
        this.status = status;
        this.entity = entity;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getEntity() {
        return entity;
    }
    
    public void setEntity(String entity) {
        this.entity = entity;
    }
    
    public boolean hasEntity() {
        return entity != null && !entity.isBlank();
    }
}
