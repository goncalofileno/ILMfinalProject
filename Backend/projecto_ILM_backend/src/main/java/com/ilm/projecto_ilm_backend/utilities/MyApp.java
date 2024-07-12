package com.ilm.projecto_ilm_backend.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * MyApp is the main class of the application.
 * It contains the main method which is the entry point of the application.
 */
public class MyApp {
    /**
     * The main method of the application.
     * It creates an ObjectMapper and registers the JavaTimeModule to it.
     * The ObjectMapper is used for converting Java objects to and from JSON.
     * The JavaTimeModule is a module for the Jackson JSON processor that handles Java 8 Date & Time API data types.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        // O restante do código da sua aplicação
    }
}

