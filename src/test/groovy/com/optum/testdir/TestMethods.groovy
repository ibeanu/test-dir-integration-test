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

import static com.jayway.restassured.RestAssured.given
import static com.jayway.restassured.config.LogConfig.logConfig
import static com.jayway.restassured.config.RestAssuredConfig.newConfig

@WebAppConfiguration
@ContextConfiguration(classes = IntegrationTestDirApplication, loader = SpringApplicationContextLoader)
@TestPropertySource(locations = "classpath:test.properties")
public class TestMethods extends Specification {

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
            .connectionConfig(ConnectionConfig.connectionConfig()))
            .baseUri(baseURI)
            .port(port)

    }

    def "get /test-methods with limit query should limit the listing"(int limit, int statusCode) {


        expect:
        Response response = dir()
            .queryParam("limit",limit)
            .get("/" + version + "/test-methods")

        response.statusCode() == statusCode
        response.contentType().equals("application/json; charset=utf-8")
        int resultsSize = response.body().jsonPath().getList("\$").size()
        assert resultsSize <= limit

        where:
        limit | statusCode
        4     | 200
    }

    def "get /test-methods should return list of test methods"() {

        expect:
        Response response = dir()
            .get("/" + version + "/test-methods")
        response.statusCode() == 200
        response.contentType().equals("application/json; charset=utf-8")
        assert response.body().jsonPath().getList("\$").size() >= 1

    }

    def "get /test-methods-autocomplete should return suggestions for test methods based on the characters typed"(String queryString, int resultNumber, int statusCode) {
        expect:
        Response response = dir()
            .queryParam("query", queryString)
            .get("/" + version + "/test-methods-autocomplete")
        response.statusCode() == statusCode
        def resultList = response.body().jsonPath().getList("\$")
        assert resultList.size().equals(resultNumber)
        for (item in resultList) {
            assert item.toString().toLowerCase().contains(queryString.toLowerCase())
        }

        where:
        queryString               | resultNumber | statusCode
        "pane"                    | 3            | 200
        "test"                    | 8            | 200
    }
}
