package com.example.shopgiayonepoly.controller.attribute;

import com.example.shopgiayonepoly.entites.Manufacturer;
import com.example.shopgiayonepoly.service.attribute.ManufacturerSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attribute")
public class ManufacturerController {

    @Autowired
    ManufacturerSevice manufacturerSevice;

    @GetMapping("/manufacturer")
    public String list(Model model) {
        model.addAttribute("manufacturerList", manufacturerSevice.findAll());
        model.addAttribute("manufacturerAdd" , new Manufacturer());
        return "Attribute/manufacturer";
    }

    @RequestMapping("/manufacturer/add")
    public String add(@ModelAttribute("manufacturerAdd") Manufacturer manufacturer){
        manufacturer.setStatus(1);
        manufacturerSevice.save(manufacturer);
        return "redirect:/attribute/manufacturer";
    }



}
