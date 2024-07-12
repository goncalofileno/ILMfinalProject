package com.ilm.projecto_ilm_backend.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

/**
 * ObjectMapperProducer is a class that provides a method to produce an ObjectMapper object.
 * It is annotated with @Singleton, meaning that it is a singleton and the same instance is used throughout the application.
 */
@Singleton
public class ObjectMapperProducer {

    /**
     * This method produces an ObjectMapper object.
     * The ObjectMapper is configured to register the JavaTimeModule and disable the WRITE_DATES_AS_TIMESTAMPS feature.
     *
     * @return an ObjectMapper object with the specified configurations
     */
    @Produces
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}

