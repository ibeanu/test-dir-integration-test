package com.optum.testdir

import com.jayway.restassured.response.Response
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

import static com.jayway.restassured.RestAssured.given
import static com.jayway.restassured.config.LogConfig.logConfig
import static com.jayway.restassured.config.RestAssuredConfig.newConfig

@WebAppConfiguration
@ContextConfiguration(classes = IntegrationTestDirApplication, loader = SpringApplicationContextLoader)
public class DemoApplicationIntegrationSpec extends Specification {

    @Value("\${baseUrl:http://162.13.50.221}")
    private String baseURI;

    @Value("\${server.port:8080}")
    private int port;


@CompileStatic
def dir() {

    given()
        .config(newConfig().logConfig(logConfig().enableLoggingOfRequestAndResponseIfValidationFails()))
        .baseUri(baseURI)
        .port(8080)
}


    def "get /dir should return a clinical indicator"(int id, int code) {

        expect:
        Response response = dir().get("/clinical-indicators/{id}", id)
        response.statusCode() == code

         where:
         id | code
         1  | 404
         2  | 404
    }

}