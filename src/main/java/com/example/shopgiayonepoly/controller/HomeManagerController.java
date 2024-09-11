package com.example.shopgiayonepoly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeManagerController {
//    @GetMapping("/")
//    public String home(){
//        return "Home/home_manege";
//    }
    @GetMapping("/")
    public String homePage(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        return "Home/home_manege";
    }
}
