package demo

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

public class DemoApplicationDemoControllerSpec extends Specification {

    private MockMvc mockMvc;

    def setup() {
        DemoController demoController = new DemoController()
        this.mockMvc =  MockMvcBuilders.standaloneSetup(demoController).build()
    }

    def "get /demo should return a message"() {
        when:
        ResultActions result = this.mockMvc.perform(get("/demo"))

        then:
        result.andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
            .andExpect(content().string("Hello demo"))
    }

}
