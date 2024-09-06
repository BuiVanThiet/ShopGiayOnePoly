package com.example.shopgiayonepoly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {
    String mess = "";
    String colorMess = "";
    @GetMapping("/")
    public String a(ModelMap modelMap) {
        modelMap.addAttribute("message",mess);
        modelMap.addAttribute("check",colorMess);
        this.mess = "";
        this.colorMess = "";
        modelMap.addAttribute("page","/Product/list");
        return "Product/list";
    }

    @GetMapping("/create")
    public String create(ModelMap modelMap) {
        modelMap.addAttribute("message",mess);
        modelMap.addAttribute("check",colorMess);
        this.mess = "";
        this.colorMess = "";
        modelMap.addAttribute("page","/Product/create");
        return "Product/create";
    }

    @GetMapping("/attribute/color")
    public String color(ModelMap modelMap) {
//        modelMap.addAttribute("message",mess);
//        modelMap.addAttribute("check",colorMess);
//        this.mess = "";
//        this.colorMess = "";
        modelMap.addAttribute("page","/Attribute/color");
        return "Attribute/color";
    }
}
