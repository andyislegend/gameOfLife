package com.avenga.config;

import com.avenga.util.PropertiesLoader;

import java.util.Properties;

public class ApplicationProperties {

    private final Properties properties;

    public ApplicationProperties() {
        properties = PropertiesLoader.load("/application.properties");
    }

    public String getProperty(String name) {
        return properties.getProperty(name);
    }

    public Boolean getBoolean(String name) {
        String property = properties.getProperty(name);
        try {
            return Boolean.parseBoolean(property);
        } catch (Exception e) {
            return true;
        }
    }

    public int getInt(String name) {
        String property = properties.getProperty(name);
        try {
            return Integer.parseInt(property);
        } catch (Exception e) {
            return 0;
        }
    }
}
