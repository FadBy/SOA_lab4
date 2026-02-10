package com.soa.lab2.service2.resource;

import com.soa.lab2.service2.ejb.Service2BusinessRemote;
import com.soa.lab2.service2.ejb.ServiceResponse;
import com.soa.lab2.service2.model.*;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/demography")
@Produces(MediaType.APPLICATION_XML)
public class DemographyResource {
    
    @EJB
    private Service2BusinessRemote business;
    
    @GET
    @Path("/nationality/{nationality}/hair-color")
    public Response getCountByNationality(@PathParam("nationality") Country nationality) {
        try {
            ServiceResponse serviceResponse = business.getCountByNationality(nationality);
            return buildResponse(serviceResponse);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error: " + e.getMessage(), 500);
            return Response.status(500).entity(error).build();
        }
    }
    
    @GET
    @Path("/nationality/{nationality}/eye-color/{eye-color}")
    public Response getCountByNationalityAndEyeColor(
            @PathParam("nationality") Country nationality,
            @PathParam("eye-color") Color eyeColor) {
        try {
            ServiceResponse serviceResponse = business.getCountByNationalityAndEyeColor(nationality, eyeColor);
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

