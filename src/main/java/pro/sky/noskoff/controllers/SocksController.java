package pro.sky.noskoff.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SocksController {

    @GetMapping
    public String helloWeb() {
        return "Hello Web!!!";
    }
}
