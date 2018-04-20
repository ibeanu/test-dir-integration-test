package com.optum.testdir

import com.jayway.restassured.response.Response

class OptimalFamilyStructures extends IntegrationTestBase {


    def "get /optimal-family-structures with limit query should limit the listing"(int limit, int statusCode) {


        expect:
        Response response = dir()
            .queryParam("limit",limit)
            .get("/" + version + "/optimal-family-structures")

        response.statusCode() == statusCode
        response.contentType().equals("application/json; charset=utf-8")
        int resultsSize = response.body().jsonPath().getList("\$").size()
        assert resultsSize <= limit

        where:
        limit | statusCode
        4     | 200
    }

    def "get /optimal-family-structures should return list of Optimal Family Structures"() {

        expect:
        Response response = dir()
            .get("/" + version + "/optimal-family-structures")
        response.statusCode() == 200
        response.contentType().equals("application/json; charset=utf-8")
        assert response.body().jsonPath().getList("\$").size() >= 1

    }

    def "get /optimal-family-structure-autocomplete should return optimal family structures for the letters typed"(String queryString, int resultNumber, int statusCode) {
        expect:
        Response response = dir()
            .queryParam("query", queryString)
            .get("/" + version + "/optimal-family-structures-autocomplete")
        response.statusCode() == statusCode
        def resultList = response.body().jsonPath().getList("\$")
        assert resultList.size().equals(resultNumber)
        for (item in resultList) {
            assert item.toString().toLowerCase().contains(queryString.toLowerCase())
        }

        where:
        queryString               | resultNumber | statusCode
        "sing"                    | 1            | 200
        "trio"                    | 2            | 200
    }
}
