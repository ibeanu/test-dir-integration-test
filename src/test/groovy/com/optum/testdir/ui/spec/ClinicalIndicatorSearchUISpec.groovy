package com.optum.testdir.ui.spec

class ClinicalIndicatorSearchUISpec extends UIIntegrationBase {

    def "search ui should contain Genomics England Heading"() {

        when:
        go baseURI + ":" + port + "/" + "clinical-indication-search"

        then:
        assert $("h1").asList().get(0).text() == "Genomics England (GE) Clinical Service Project"
    }

    def "search for disease on free text search field should return information on the disease"(String query, String heading) {

        when:
        go baseURI + ":" + port + "/" + "clinical-indication-search"

        and:
        $("input").asList().get(5).value(query)

        then:
        waitFor(1) {
            $('#autocomplete-btn').click()
            $(".results > #clinicalindicator0 > p").asList().get(0).text() == heading
        }

        where:
        query|heading
        "cancer"|"CDH1-related cancer syndrome"
    }

}
