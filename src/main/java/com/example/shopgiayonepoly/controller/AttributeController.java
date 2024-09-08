package com.example.shopgiayonepoly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attribute")
public class AttributeController {

    @RequestMapping("/brand")
    public String brand(ModelMap modelMap){
        modelMap.addAttribute("brand", "/Attribute/brand");
        return "Attribute/Brand";
    }

    @RequestMapping("/color")
    public String color(ModelMap modelMap){
        modelMap.addAttribute("color", "/Attribute/color");
        return "Attribute/Color";
    }

    @RequestMapping("/material")
    public String material(ModelMap modelMap){
        modelMap.addAttribute("material", "/Attribute/material");
        return "Attribute/material";
    }

    @RequestMapping("/porcelain")
    public String porcelain(ModelMap modelMap){
        modelMap.addAttribute("porcelain", "/Attribute/porcelain");
        return "Attribute/Porcelain";
    }

    @RequestMapping("/size")
    public String size(ModelMap modelMap){
        modelMap.addAttribute("size", "/Attribute/size");
        return "Attribute/Size";
    }
}
