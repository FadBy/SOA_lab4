package com.soa.lab2.service2.resource;

import com.soa.lab2.service2.ejb.Service2BusinessRemote;
import com.soa.lab2.service2.ejb.ServiceResponse;
import com.soa.lab2.service2.model.*;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/persons")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
public class PersonResource {
    
    @EJB
    private Service2BusinessRemote business;
    
    @GET
    public Response getAllPersons(
            @QueryParam("sort") String sort,
            @QueryParam("filter") String filter,
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("size") @DefaultValue("10") Integer size) {
        
        try {
            ServiceResponse serviceResponse = business.getPersons(sort, filter, page, size);
            return buildResponse(serviceResponse);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error: " + e.getMessage(), 500);
            return Response.status(500).entity(error).build();
        }
    }
    
    @POST
    public Response createPerson(Person person) {
        try {
            ServiceResponse serviceResponse = business.createPerson(person);
            return buildResponse(serviceResponse);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error: " + e.getMessage(), 500);
            return Response.status(500).entity(error).build();
        }
    }
    
    @GET
    @Path("/{id}")
    public Response getPersonById(@PathParam("id") Long id) {
        try {
            ServiceResponse serviceResponse = business.getPersonById(id);
            return buildResponse(serviceResponse);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error: " + e.getMessage(), 500);
            return Response.status(500).entity(error).build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response updatePerson(@PathParam("id") Long id, Person person) {
        try {
            ServiceResponse serviceResponse = business.updatePerson(id, person);
            return buildResponse(serviceResponse);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error: " + e.getMessage(), 500);
            return Response.status(500).entity(error).build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") Long id) {
        try {
            ServiceResponse serviceResponse = business.deletePerson(id);
            return buildResponse(serviceResponse);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error: " + e.getMessage(), 500);
            return Response.status(500).entity(error).build();
        }
    }
    
    @GET
    @Path("/max-coordinates")
    public Response getPersonWithMaxCoordinates() {
        try {
            ServiceResponse serviceResponse = business.getPersonWithMaxCoordinates();
            return buildResponse(serviceResponse);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error: " + e.getMessage(), 500);
            return Response.status(500).entity(error).build();
        }
    }
    
    @GET
    @Path("/search")
    public Response searchPersonsByName(@QueryParam("name") String name) {
        try {
            ServiceResponse serviceResponse = business.searchPersonsByName(name);
            return buildResponse(serviceResponse);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error: " + e.getMessage(), 500);
            return Response.status(500).entity(error).build();
        }
    }
    
    @GET
    @Path("/locations")
    public Response getAllLocations() {
        try {
            ServiceResponse serviceResponse = business.getAllLocations();
            return buildResponse(serviceResponse);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error: " + e.getMessage(), 500);
            return Response.status(500).entity(error).build();
        }
    }
    
    private Response buildResponse(ServiceResponse serviceResponse) {
        if (serviceResponse == null) {
            return Response.status(500).build();
        }
        if (serviceResponse.hasEntity()) {
            return Response.status(serviceResponse.getStatus())
                    .entity(serviceResponse.getEntity())
                    .build();
        }
        return Response.status(serviceResponse.getStatus()).build();
    }
}

