package demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class DemoController {

    @RequestMapping("/demo")
    @ResponseBody
    public String demo() {
        return "Hello demo";
    }

    @RequestMapping(value = "/demo", params = {"name"})
    @ResponseBody
    public String demo(@RequestParam String name) {
        return "Hello " + name;
    }

}
