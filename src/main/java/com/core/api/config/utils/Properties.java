package com.core.api.config.utils;

import java.io.IOException;
import java.io.InputStream;

public class Properties {
    public static java.util.Properties getProperties() throws IOException {
        java.util.Properties properties = new java.util.Properties();
        try (InputStream input = Properties.class.getResourceAsStream("/application.properties")) {
            if (input == null) {
                throw new IOException("El archivo de propiedades 'application.properties' no se encuentra.");
            }
            properties.load(input);
        }
        return properties;
    }
}
