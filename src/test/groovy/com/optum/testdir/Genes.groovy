package com.optum.testdir

import io.restassured.response.Response

class Genes extends IntegrationTestBase {

    def "get /genes with limit query should limit the listing"(int limit, int statusCode) {

        expect:
        Response response = dir()
            .queryParam("limit",limit)
            .get("/" + version + "/genes")

        response.statusCode() == statusCode
        response.contentType().equals("application/json; charset=utf-8")
        int resultsSize = response.body().jsonPath().getList("\$").size()
        assert resultsSize <= limit

        where:
        limit | statusCode
        4     | 200
    }

    def "get /genes should return list of target genes"() {

        expect:
        Response response = dir()
            .get("/" + version + "/genes")
        response.statusCode() == 200
        response.contentType().equals("application/json; charset=utf-8")
        assert response.body().jsonPath().getList("\$").size() >= 1

    }

    def "get /genes-autocomplete should return suggestions for target genes based on the characters typed"(String queryString, int statusCode) {
        expect:
        Response response = dir()
            .queryParam("query", queryString)
            .get("/" + version + "/genes-autocomplete")
        response.statusCode() == statusCode
        def resultList = response.body().jsonPath().getList("\$")
        for (item in resultList) {
            assert item.toString().toLowerCase().contains(queryString.toLowerCase())
        }

        where:
        queryString               | statusCode
        "BRCA"                    | 200
        "panel"                   | 200
        "inva"                    | 200 //invalid query should return no results
    }
}
