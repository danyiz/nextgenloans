package hu.restapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HelloController {

    @RequestMapping("/hello")
    public String hello() {
        log.debug("Hello is called");
        return "Hello World RESTful with Spring Boot";
    }
}
