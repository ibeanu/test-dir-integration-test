package com.optum.testdir

import com.jayway.restassured.config.ConnectionConfig
import com.jayway.restassured.response.Response
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

import java.util.concurrent.TimeUnit

import static com.jayway.restassured.RestAssured.expect
import static com.jayway.restassured.RestAssured.given
import static com.jayway.restassured.config.LogConfig.logConfig
import static com.jayway.restassured.config.RestAssuredConfig.newConfig

@WebAppConfiguration
@ContextConfiguration(classes = IntegrationTestDirApplication, loader = SpringApplicationContextLoader)
@TestPropertySource(locations = "classpath:test.properties")
public class WGSGroupingsAutoComplete extends Specification {

    @Value("\${baseUrl}")
    private String baseURI;

    @Value("\${server.port}")
    private int port;

    @Value("\${version}")
    private String version


    @CompileStatic
    def dir() {


        given()
            .config(newConfig().logConfig(logConfig().enableLoggingOfRequestAndResponseIfValidationFails())
            .connectionConfig(ConnectionConfig.connectionConfig()
            .closeIdleConnectionsAfterEachResponseAfter(20, TimeUnit.SECONDS)))
            .baseUri(baseURI)
            .port(port)

    }

    def "get /wgs-groupings with limit query should limit the "(int limit, int statusCode) {


        expect:
        Response response = dir()
            .queryParam("limit",limit)
            .get("/" + version + "/wgs-groupings")

        response.statusCode() == statusCode
        response.contentType().equals("application/json; charset=utf-8")
        int resultsSize = response.body().jsonPath().getList("\$").size()
        assert resultsSize <= limit

        where:
        limit | statusCode
        4     | 200
    }

    def "get /wgs-groupings should return WGS grouping entries"() {

        expect:
        Response response = dir()
            .get("/" + version + "/wgs-groupings")
        response.statusCode() == 200
        response.contentType().equals("application/json; charset=utf-8")
        assert response.body().jsonPath().getList("\$").size() >= 1

    }

    def "get /wgs-groupings-autocomplete should return WGS grouping entries"(String groupingString, int resultNumber, int statusCode) {
        expect:
        Response response = dir()
            .queryParam("query", groupingString)
            .get("/" + version + "/wgs-groupings-autocomplete")
        response.statusCode() == statusCode
        def resultList = response.body().jsonPath().getList("\$")
        assert resultList.size().equals(resultNumber)
        for (item in resultList) {
            assert item.toString().toLowerCase().contains(groupingString.toLowerCase())
        }

        where:
        groupingString            | resultNumber | statusCode
        "Intellectual disability" | 1            | 200
        "Aortopathy"              | 1            | 200
        "atypical"                | 2            | 200
        "dis"                     | 10           | 200
        "invalidquerystring"      | 0            | 200
    }
}
