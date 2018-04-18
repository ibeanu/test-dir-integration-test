package com.optum.testdir

import com.jayway.restassured.config.ConnectionConfig
import com.jayway.restassured.path.json.config.JsonPathConfig
import com.jayway.restassured.response.Response
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.context.annotation.PropertySource
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

import java.util.concurrent.TimeUnit

import static com.jayway.restassured.RestAssured.given
import static com.jayway.restassured.config.LogConfig.logConfig
import static com.jayway.restassured.config.RestAssuredConfig.newConfig
import static com.optum.testdir.Constants.VERSION

@WebAppConfiguration
@ContextConfiguration(classes = IntegrationTestDirApplication, loader = SpringApplicationContextLoader)
@TestPropertySource(locations = "classpath:test.properties")
public class ClinicalIndicators extends Specification {

    @Value("\${baseUrl}")
    private String baseURI;

    @Value("\${server.port}")
    private int port;


    @CompileStatic
    def dir() {

        given()
            .config(newConfig().logConfig(logConfig().enableLoggingOfRequestAndResponseIfValidationFails())
            .connectionConfig(ConnectionConfig.connectionConfig()
            .closeIdleConnectionsAfterEachResponseAfter(20, TimeUnit.SECONDS)))
            .baseUri(baseURI)
            .port(port)
    }


    def "get /dir should return a clinical indicator"(int id, int statusCode, int serviceGroupId,
                                                      String indicationId, String testId, String testName,
                                                      String technology, String targetGene) {

        expect:
        Response response = dir().get("/" + VERSION + "/clinical-indicators/{id}", id)
        response.statusCode() == statusCode
        response.contentType().equals("application/json; charset=utf-8")

        Integer.valueOf(response.body().jsonPath().getString("specialist_service_group_id")) == serviceGroupId
        response.body().jsonPath().getString("test_ID_code").equals(testId)
        response.body().jsonPath().getString("test_name").equals(testName)
        response.body().jsonPath().get("technology").equals(technology)
        response.body().jsonPath().get("target_gene").equals(targetGene)
        response.body().jsonPath().get("clinical_indication_ID_code").equals(indicationId)

        where:
        id   | statusCode | serviceGroupId | indicationId | testId   | testName                 | technology                       | targetGene
        1001 | 200        | 93             | "C153"       | "C153.6" | "BRAF-AKAP9 FISH/RT-PCR" | "FISH/Targeted mutation testing" | "BRAF-AKAP9"
    }

}
