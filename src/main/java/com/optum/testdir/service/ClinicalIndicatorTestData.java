package com.optum.testdir.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

public class ClinicalIndicatorTestData implements DataService {
    @Override
    public Map<String, String> loadExpectedResponses() throws URISyntaxException, IOException {

        Path path = Paths.get(getClass().getClassLoader()
            .getResource("test.csv").toURI());

        StringBuilder data = new StringBuilder();
        Stream<String> lines = Files.lines(path);

        lines.forEach(line -> {
            data.append(line).append("\n");
        });
        lines.close();

        return null;
    }
}
