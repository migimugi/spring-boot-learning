package com.example.spring_boot_learning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HelloController {
    
    @GetMapping("/hello")
    public String hello(
        @RequestParam(name = "name",
            defaultValue = "World"
        ) String userName) {
        return "Hello, " + userName + "!";
    }

    @GetMapping("/greeting")
    public GreetingResponse greeting(
        @RequestParam(
            name = "name",
            defaultValue = "World"
        ) String userName){
            return new GreetingResponse(
                "Hello, " + userName + "!"
            );
        }
}
