package ru.aveskin.ordermicroservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class TestController {

    @GetMapping("/test")
    public String Test(){
      return "Test1";
    }

}
