package com.optum.testdir.ui.spec

import com.optum.testdir.IntegrationTestDirApplication
import geb.spock.GebSpec
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.web.WebAppConfiguration

@WebAppConfiguration
@ContextConfiguration(classes = IntegrationTestDirApplication)
@TestPropertySource(locations = "classpath:test.properties")
@ActiveProfiles("DEV")
class UIIntegrationBase extends GebSpec{

    @Value("\${baseUrl}")
    String baseURI;

    @Value("\${server.port}")
    int port;
}
