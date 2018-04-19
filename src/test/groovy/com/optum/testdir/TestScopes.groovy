package com.optum.testdir

import com.google.common.collect.ImmutableMap
import com.jayway.restassured.config.ConnectionConfig
import com.jayway.restassured.response.Response
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
class TestScopes extends Specification {
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

    def "get /test-scopes should return a list of test scopes"(List<Map<String, String>> result, int statusCode) {

        expect:
        Response response = dir()
            .get("/" + version + "/test-scopes")

        response.statusCode() == statusCode
        response.contentType().equals("application/json; charset=utf-8")

        def responseList = response.body().jsonPath().getList("")

        responseList.get(0).getAt("description").equals(result.get(0).get("description"))
        responseList.get(0).getAt("id").toString().equals(result.get(0).get("id"))
        responseList.get(0).getAt("scope").equals(result.get(0).get("scope"))


        where:
        result                | statusCode
        getTestScopeResults() | 200
    }

    def "get /test-scopes-autocomplete with query on scope should return a scope info"(List<String> result, String query,
                                                                                       int statusCode) {

        expect:
        Response response = dir()
            .queryParam("query", query)
            .get("/" + version + "/test-scopes-autocomplete")

        response.statusCode() == statusCode
        response.contentType().equals("application/json; charset=utf-8")

        def responseList = response.body().jsonPath().getList("")

        responseList.get(0).equals(result.get(0))


        where:
        result               | query | statusCode
        getListOfScopeInfo() | "str" | 200
    }


    def getTestScopeResults() {
        return Arrays.asList(
            ImmutableMap.of("description", "Small variant detection", "id", "1", "scope", "snv"),
            ImmutableMap.of("description", "Copy number variant detection to genomewide resolution", "id", "2",
                "scope", "cnvgenomewide"),
            ImmutableMap.of("description", "Copy number variant detection to exon level resolution", "id", "3",
                "scope", "cnvexonlevel")
        )
    }

    def getListOfScopeInfo() {
        return Arrays.asList(
            "str", "Structural variant detection", "Structural variant detection", "Structural variant detection"
        )
    }
}
