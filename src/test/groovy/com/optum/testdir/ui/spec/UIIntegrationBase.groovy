package com.optum.testdir.ui.spec

import com.optum.testdir.IntegrationTestDirApplication
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.context.annotation.Profile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.web.WebAppConfiguration

import geb.spock.GebSpec

@WebAppConfiguration
@ContextConfiguration(classes = IntegrationTestDirApplication, loader = SpringApplicationContextLoader)
@TestPropertySource(locations = "classpath:test.properties")
@ActiveProfiles("DEV")
class UIIntegrationBase extends GebSpec{

    @Value("\${baseUrl}")
    String baseURI;

    @Value("\${server.port}")
    int port;
}
