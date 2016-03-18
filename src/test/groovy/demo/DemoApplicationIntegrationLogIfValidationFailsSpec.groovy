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
import static org.hamcrest.core.IsEqual.equalTo

@WebAppConfiguration
@ContextConfiguration(classes = DemoApplication, loader = SpringApplicationContextLoader)
@IntegrationTest("server.port=0")
public class DemoApplicationIntegrationLogIfValidationFailsSpec extends Specification {

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
        expectResponse(response) {
            assert statusCode() == 200
            assert contentType() =~ /\A${MediaType.TEXT_PLAIN}.*/
            assert asString() == "Hello demo"
        }
    }

    def "get /demo with name should return a message"() {
        when:
        Response response = demo()
            .param("name", "abc")
            .get("/demo")

        then:
        expectResponse(response) {
            assert statusCode() == 200
            assert contentType() =~ /\A${MediaType.TEXT_PLAIN}.*/
            assert asString() == "Hello abc"
        }
    }

    @CompileStatic
    def void expectResponse(Response response, @DelegatesTo(Response) Closure closure) {
        try {
            with(response, closure)
        } catch (AssertionError e) {
            triggerRestAssuredLogging(response)
            throw e
        }
    }

    @CompileStatic
    private void triggerRestAssuredLogging(Response response) {
        try {
            response.then().body(equalTo("Some value to trigger a failure which will trigger rest assured ifValidationFails logging"))
        } catch (AssertionError ex) {
            // suppress this failure
        }
    }

}
