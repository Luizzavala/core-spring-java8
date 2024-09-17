package com.core.api.config.utils;

import lombok.experimental.UtilityClass;

import java.util.Properties;

@UtilityClass
public class PropertiesUtils {
    public static Properties getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(PropertiesUtils.class
                    .getResourceAsStream("/application.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }
}
