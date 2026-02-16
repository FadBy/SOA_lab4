package com.soa.lab4.service1.proxy.client;

import com.soa.lab4.service1.proxy.model.Color;
import com.soa.lab4.service1.proxy.model.Country;
import com.soa.lab4.service1.proxy.model.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Service1BusClient {
    private final RestTemplate restTemplate;
    private final String busInvokeUrl;

    public Service1BusClient(
            @Value("${mule.base-url}") String muleBaseUrl,
            @Value("${mule.soap-invoke-path}") String soapInvokePath
    ) {
        this.restTemplate = new RestTemplate();
        this.busInvokeUrl = muleBaseUrl + soapInvokePath;
    }

    public String getPersons(String sort, String filter, Integer page, Integer size) {
        StringBuilder body = new StringBuilder();
        if (sort != null) body.append(tag("sort", escapeXml(sort)));
        if (filter != null) body.append(tag("filter", escapeXml(filter)));
        if (page != null) body.append(tag("page", String.valueOf(page)));
        if (size != null) body.append(tag("size", String.valueOf(size)));
        return invoke("getPersons", body.toString());
    }

    public String createPerson(Person person) {
        return invoke("createPerson", tag("person", personToXml(person, false)));
    }

    public String getPersonById(Long id) {
        return invoke("getPersonById", tag("id", String.valueOf(id)));
    }

    public String updatePerson(Long id, Person person) {
        return invoke("updatePerson", tag("id", String.valueOf(id)) + tag("person", personToXml(person, true)));
    }

    public boolean deletePerson(Long id) {
        String xml = invoke("deletePerson", tag("id", String.valueOf(id)));
        return xml.contains("<deleted>true</deleted>");
    }

    public String getPersonWithMaxCoordinates() {
        return invoke("getPersonWithMaxCoordinates", "");
    }

    public String searchPersonsByName(String name) {
        return invoke("searchPersonsByName", tag("name", escapeXml(name)));
    }

    public String getAllLocations() {
        return invoke("getAllLocations", "");
    }

    public String getCountByNationality(Country nationality) {
        return invoke("getCountByNationality", tag("nationality", nationality.name()));
    }

    public String getCountByNationalityAndEyeColor(Country nationality, Color eyeColor) {
        return invoke("getCountByNationalityAndEyeColor", tag("nationality", nationality.name()) + tag("eyeColor", eyeColor.name()));
    }

    private String invoke(String operation, String bodyXml) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> request = new HttpEntity<>(bodyXml, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(busInvokeUrl + "/" + operation, request, String.class);
        return response.getBody() == null ? "" : response.getBody();
    }

    private String personToXml(Person person, boolean includeId) {
        StringBuilder xml = new StringBuilder();
        if (includeId && person.getId() != null) xml.append(tag("id", String.valueOf(person.getId())));
        xml.append(tag("name", escapeXml(person.getName())));
        if (person.getCoordinates() != null) {
            xml.append(tag("coordinates", tag("x", String.valueOf(person.getCoordinates().getX())) + tag("y", String.valueOf(person.getCoordinates().getY()))));
        }
        if (person.getBirthday() != null) xml.append(tag("birthday", person.getBirthday().toString()));
        if (person.getHeight() != null) xml.append(tag("height", String.valueOf(person.getHeight())));
        if (person.getEyeColor() != null) xml.append(tag("eyeColor", person.getEyeColor().name()));
        if (person.getNationality() != null) xml.append(tag("nationality", person.getNationality().name()));
        if (person.getLocation() != null) {
            xml.append(tag("location",
                    tag("x", String.valueOf(person.getLocation().getX())) +
                    tag("y", String.valueOf(person.getLocation().getY())) +
                    tag("name", escapeXml(person.getLocation().getName()))));
        }
        return xml.toString();
    }

    private String tag(String name, String value) {
        return "<" + name + ">" + (value == null ? "" : value) + "</" + name + ">";
    }

    private String escapeXml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
