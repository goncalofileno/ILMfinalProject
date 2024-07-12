package com.ilm.projecto_ilm_backend.utilities;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;
/**
 * JacksonConfig is a class that configures the ObjectMapper for the application.
 * It implements the ContextResolver interface and is annotated with @Provider, meaning that it is a provider of context information to resource classes and other providers.
 * The ObjectMapper is configured to register the JavaTimeModule and disable the WRITE_DATES_AS_TIMESTAMPS feature.
 */
@Provider
public class JacksonConfig implements ContextResolver<ObjectMapper> {
    /**
     * ObjectMapper object used for converting Java objects to and from JSON.
     */
    private final ObjectMapper mapper;
    /**
     * Constructor for the JacksonConfig class.
     * It initializes the ObjectMapper and configures it.
     */
    public JacksonConfig() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    /**
     * This method returns the ObjectMapper object.
     * It is an implementation of the getContext method from the ContextResolver interface.
     *
     * @param type the class of the object for which the context is to be provided
     * @return the ObjectMapper object
     */
    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
