package com.avenga.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertiesLoader {

    private static final String DEFAULT_PROPERTIES_DELIMITER = "=";

    public static Properties load(String resourceName) {
        URL resourceUrl = PropertiesLoader.class.getResource(resourceName);
        try {
            Path path = Paths.get(resourceUrl.toURI());
            try (Stream<String> lines = Files.lines(path)) {
                return lines.
                        map(line -> line.split(DEFAULT_PROPERTIES_DELIMITER))
                        .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1], (e1, e2) -> e1, Properties::new));
            }
        } catch (Exception e) {
            //do nothing
        }
        return new Properties();
    }
}
