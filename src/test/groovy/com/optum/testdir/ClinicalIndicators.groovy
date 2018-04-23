package com.optum.testdir

import io.restassured.path.json.exception.JsonPathException
import io.restassured.response.Response

class ClinicalIndicators extends IntegrationTestBase {

    def "get /clinical-indicators/{id} should return a clinical indicator"(int id, int statusCode, int serviceGroupId,
                                                                           String indicationId, String testId, String testName,
                                                                           String technology, String targetGene,
                                                                           String contentType) {
        expect:
        Response response;
        try {
            response = dir().get("/" + version + "/clinical-indicators/{id}", id)
            response.statusCode() == statusCode
            response.contentType().equals(contentType)

            Integer.valueOf(response.body().jsonPath().getString("specialist_service_group_id")) == serviceGroupId
            response.body().jsonPath().getString("test_ID_code").equals(testId)
            response.body().jsonPath().getString("test_name").equals(testName)
            response.body().jsonPath().get("technology").equals(technology)
            response.body().jsonPath().get("target_gene").equals(targetGene)
            response.body().jsonPath().get("clinical_indication_ID_code").equals(indicationId)

        } catch (JsonPathException e ) {
            response.contentType().equals(contentType)
        }

        where:
        id   | statusCode | serviceGroupId | indicationId | testId   | testName                 | technology                       | targetGene   | contentType
        1001 | 200        | 93             | "C153"       | "C153.6" | "BRAF-AKAP9 FISH/RT-PCR" | "FISH/Targeted mutation testing" | "BRAF-AKAP9" | "application/json; charset=utf-8"
        1002 | 200        | 93             | "C153"       | "C153.7" | "BRAF-CCDC6 FISH/RT-PCR" | "FISH/Targeted mutation testing" | "BRAF-CCDC6" | "application/json; charset=utf-8"
        1    | 504        | 0              | null         | null     | null                     | null                             | null         | "text/plain; charset=utf-8"
    }

    def "get /clinical-indicators-search with query should return list of clinical-indicators"(String query, String code,
                                                                                               String structure, String indication,
                                                                                               int statusCode) {
        expect:
        Response response = dir()
            .queryParam("query", query)
            .get("/" + version + "/clinical-indicators-search")

        response.statusCode() == statusCode
        response.contentType().equals("application/json; charset=utf-8")

        def results = response.body().jsonPath().getList("")

        results.get(0).getAt("optimal_family_structure").getAt("structure").equals(structure)
        results.get(0).getAt("code").equals(code)
        results.get(0).getAt("indication").equals(indication)

        where:
        query    | code       | structure   | indication                     | statusCode
        "cancer" | "100162.0" | "singleton" | "CDH1-related cancer syndrome" | 200
    }

    def "get /clinical-indicators-autocomplete with query should return list of autocomplete clinical-indicators"(String query,
                                                                                                                  List<String> result,
                                                                                                                  int statusCode) {
        expect:
        Response response = dir()
            .queryParam("query", query)
            .get("/" + version + "/clinical-indicators-autocomplete")

        response.statusCode() == statusCode
        response.contentType().equals("application/json; charset=utf-8")

        def responseList = response.body().jsonPath().getList("")
        responseList.equals(result)
        responseList.get(0).equals(result.get(0))

        where:
        query    | result                            | statusCode
        "cancer" | getAutoCompletResponseForCancer() | 200
    }

    def getAutoCompletResponseForCancer() {
        return Arrays.asList("Inherited breast cancer (without ovarian cancer) at high familial risk levels",
            "Inherited breast cancer and ovarian cancer at high familial risk levels",
            "CDH1-related cancer syndrome",
            "Non-Small Cell Lung Cancer",
            "Breast cancer",
            "Inherited ovarian cancer",
            "DICER1-related cancer predisposition",
            "Non-Small Cell Lung Cancer",
            "Non-Small Cell Lung Cancer",
            "Non-Small Cell Lung Cancer")
    }
}
