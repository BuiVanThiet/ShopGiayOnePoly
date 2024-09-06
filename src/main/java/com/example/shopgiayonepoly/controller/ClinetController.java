package com.example.shopgiayonepoly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/benKH")
public class ClinetController {
    @GetMapping("/home")
    public String getTem() {
        return "client/homepage";
    }
}
