package com.optum.testdir

import com.jayway.restassured.config.ConnectionConfig
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

import static com.jayway.restassured.RestAssured.given
import static com.jayway.restassured.config.LogConfig.logConfig
import static com.jayway.restassured.config.RestAssuredConfig.newConfig

@WebAppConfiguration
@ContextConfiguration(classes = IntegrationTestDirApplication, loader = SpringApplicationContextLoader)
@TestPropertySource(locations = "classpath:test.properties")
class IntegrationTestBase extends Specification {

    @Value("\${baseUrl}")
    String baseURI;

    @Value("\${server.port}")
    int port;

    @Value("\${version}")
    String version


    @CompileStatic
    def dir() {

        given()
            .config(newConfig().logConfig(logConfig().enableLoggingOfRequestAndResponseIfValidationFails())
            .connectionConfig(ConnectionConfig.connectionConfig()))
            .baseUri(baseURI)
            .port(port)
    }

}
