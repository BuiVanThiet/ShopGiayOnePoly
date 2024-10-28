package com.example.shopgiayonepoly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping ("/forgotPassword")
public class forgotPasswordController {
    @GetMapping("/form")
    public String formForgotPassword(){
        return "client/forgotPassword";
    }
}
