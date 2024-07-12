package com.ilm.projecto_ilm_backend.security;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

/**
 * CorsFilter is a class that implements the ContainerResponseFilter interface.
 * It is annotated with @Provider, meaning that it is a provider of context information to resource classes and other providers.
 * The filter method adds CORS headers to the response, allowing cross-origin requests from the specified origin.
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

    /**
     * This method adds CORS headers to the response.
     * It allows cross-origin requests from the specified origin, with the specified methods and headers.
     * It also allows credentials to be included in the requests.
     *
     * @param requestContext the request context
     * @param responseContext the response context
     * @throws IOException if an I/O exception occurs
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}