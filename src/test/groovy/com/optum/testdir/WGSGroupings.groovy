package com.optum.testdir

import com.jayway.restassured.response.Response

class WGSGroupings extends IntegrationTestBase {

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
