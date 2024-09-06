package com.example.shopgiayonepoly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/onepoly")
public class ClientController {
    @GetMapping("/base")
    public String getFormHomeClientBase(){
        return "client/base";
    }
    @GetMapping("/home")
    public String getFormHomeClient(){
        return "client/homepage";
    }
    @GetMapping("/address")
    public String getCallAPIGHN(){
        return "client/address";
    }
}
