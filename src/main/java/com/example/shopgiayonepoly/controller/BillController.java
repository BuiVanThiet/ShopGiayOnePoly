package com.example.shopgiayonepoly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bill")
public class BillController {
    @GetMapping("/")
    public String getForm(ModelMap modelMap) {
        modelMap.addAttribute("page","/Bill/index");
        return "Home/home";
    }
}
