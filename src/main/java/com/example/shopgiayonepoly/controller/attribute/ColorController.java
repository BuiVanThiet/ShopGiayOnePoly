package com.example.shopgiayonepoly.controller.attribute;

import com.example.shopgiayonepoly.entites.Color;
import com.example.shopgiayonepoly.service.ProductSevice;
import com.example.shopgiayonepoly.service.attribute.ColorSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attribute")
public class ColorController {

    @Autowired
    ColorSevice colorSevice;

    @GetMapping("/color")
    public String list(Model model) {
        model.addAttribute("colorList", colorSevice.findAll());
        model.addAttribute("colorAdd" , new Color());
        return "Attribute/color";
    }

    @RequestMapping("/color/add")
    public String add(@ModelAttribute("colorAdd") Color color){
        color.setStatus(1);
        colorSevice.save(color);
        return "redirect:/attribute/color";
    }



}
