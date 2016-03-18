package demo

import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

@WebAppConfiguration
@ContextConfiguration(classes = DemoApplication, loader = SpringApplicationContextLoader)
public class DemoApplicationSpec extends Specification {

    def "context should load"() {
        expect:
        true
    }

}
