package com.soa.lab2.service2.config;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class CorsFilter implements ContainerResponseFilter, ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
			Response.ResponseBuilder rb = Response.ok();
			addCorsHeaders(rb);
			requestContext.abortWith(rb.build());
		}
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		addCorsHeaders(responseContext);
	}

	private void addCorsHeaders(ContainerResponseContext response) {
		response.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
		response.getHeaders().putSingle("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
		response.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type,Accept");
		response.getHeaders().putSingle("Access-Control-Max-Age", "86400");
	}

	private void addCorsHeaders(Response.ResponseBuilder rb) {
		rb.header("Access-Control-Allow-Origin", "*")
		  .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
		  .header("Access-Control-Allow-Headers", "Content-Type,Accept")
		  .header("Access-Control-Max-Age", "86400");
	}
}

