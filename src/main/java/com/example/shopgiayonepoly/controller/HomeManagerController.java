package com.example.shopgiayonepoly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeManagerController {
    @GetMapping("/")
    public String home(){
        return "Home/home_manege";
    }
}
