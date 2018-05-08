package com.optum.testdir.config;

import com.optum.testdir.service.ClinicalIndicatorTestData;
import com.optum.testdir.service.DataService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
    @Bean
    public DataService dataService() {
        return new ClinicalIndicatorTestData();
    }
}
