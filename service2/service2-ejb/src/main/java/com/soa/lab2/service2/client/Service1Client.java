package com.soa.lab2.service2.client;

import com.soa.lab2.service2.model.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;

public class Service1Client {
    
    private static final String SERVICE1_BASE_URL = System.getProperty("service1.url", "https://localhost:8444/service1-spring");
    private final Client client;
    
    public Service1Client() {
        // Create client that accepts self-signed certificates
        this.client = createClientAcceptingSelfSignedCertificates();
    }
    
    private Client createClientAcceptingSelfSignedCertificates() {
        try {
            // Create a trust manager that accepts all certificates
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };
            
            // Install the all-trusting trust manager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            
            HostnameVerifier allHostsValid = (hostname, session) -> true;
            
            return ClientBuilder.newBuilder()
                    .sslContext(sslContext)
                    .hostnameVerifier(allHostsValid)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create HTTP client", e);
        }
    }
    
    public Response getPersons(String sort, String filter, Integer page, Integer size) {
        return client.target(SERVICE1_BASE_URL + "/persons")
                .queryParam("sort", sort)
                .queryParam("filter", filter)
                .queryParam("page", page != null ? page : 0)
                .queryParam("size", size != null ? size : 10)
                .request(MediaType.APPLICATION_XML)
                .get();
    }
    
    public Response createPerson(Person person) {
        return client.target(SERVICE1_BASE_URL + "/persons")
                .request(MediaType.APPLICATION_XML)
                .post(Entity.entity(person, MediaType.APPLICATION_XML));
    }
    
    public Response getPersonById(Long id) {
        return client.target(SERVICE1_BASE_URL + "/persons/" + id)
                .request(MediaType.APPLICATION_XML)
                .get();
    }
    
    public Response updatePerson(Long id, Person person) {
        return client.target(SERVICE1_BASE_URL + "/persons/" + id)
                .request(MediaType.APPLICATION_XML)
                .put(Entity.entity(person, MediaType.APPLICATION_XML));
    }
    
    public Response deletePerson(Long id) {
        return client.target(SERVICE1_BASE_URL + "/persons/" + id)
                .request()
                .delete();
    }
    
    public Response getPersonWithMaxCoordinates() {
        return client.target(SERVICE1_BASE_URL + "/persons/max-coordinates")
                .request(MediaType.APPLICATION_XML)
                .get();
    }
    
    public Response searchPersonsByName(String name) {
        return client.target(SERVICE1_BASE_URL + "/persons/search")
                .queryParam("name", name)
                .request(MediaType.APPLICATION_XML)
                .get();
    }
    
    public Response getAllLocations() {
        return client.target(SERVICE1_BASE_URL + "/persons/locations")
                .request(MediaType.APPLICATION_XML)
                .get();
    }
    
    public Response getCountByNationality(Country nationality) {
        return client.target(SERVICE1_BASE_URL + "/demography/nationality/" + nationality + "/hair-color")
                .request(MediaType.APPLICATION_XML)
                .get();
    }
    
    public Response getCountByNationalityAndEyeColor(Country nationality, Color eyeColor) {
        return client.target(SERVICE1_BASE_URL + "/demography/nationality/" + nationality + "/eye-color/" + eyeColor)
                .request(MediaType.APPLICATION_XML)
                .get();
    }
    
    public void close() {
        if (client != null) {
            client.close();
        }
    }
}

