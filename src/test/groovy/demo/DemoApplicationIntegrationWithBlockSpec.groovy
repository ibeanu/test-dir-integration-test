package demo

import com.jayway.restassured.response.Response
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

import static com.jayway.restassured.RestAssured.given
import static com.jayway.restassured.config.LogConfig.logConfig
import static com.jayway.restassured.config.RestAssuredConfig.newConfig

@WebAppConfiguration
@ContextConfiguration(classes = DemoApplication, loader = SpringApplicationContextLoader)
@IntegrationTest("server.port=0")
public class DemoApplicationIntegrationWithBlockSpec extends Specification {

    @Value("\${local.server.port}")
    private int port;

    @CompileStatic
    def demo() {
        given()
            .config(newConfig().logConfig(logConfig().enableLoggingOfRequestAndResponseIfValidationFails()))
            .baseUri("http://localhost")
            .port(port)
    }

    def "get /demo should return a message"() {
        when:
        Response response = demo().get("/demo")

        then:
        with(response) {
            statusCode() == 200
            contentType() =~ /\A${MediaType.TEXT_PLAIN}.*/
            asString() == "Hello demo"
        }
    }

    def "get /demo with name should return a message"() {
        when:
        Response response = demo()
            .param("name", "abc")
            .get("/demo")

        then:
        with(response) {
            statusCode() == 200
            contentType() =~ /\A${MediaType.TEXT_PLAIN}.*/
            asString() == "Hello abc"
        }
    }

}
