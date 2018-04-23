package com.optum.testdir

import com.google.common.collect.ImmutableMap
import com.jayway.restassured.response.Response

class TestScopes extends IntegrationTestBase {

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
