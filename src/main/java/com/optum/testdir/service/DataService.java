package com.optum.testdir.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
@Service
public interface DataService {
    Map<String, String> loadExpectedResponses() throws URISyntaxException, IOException;
}
