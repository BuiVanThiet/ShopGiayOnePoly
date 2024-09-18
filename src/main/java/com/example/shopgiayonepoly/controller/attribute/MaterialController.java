package com.example.shopgiayonepoly.controller.attribute;

import com.example.shopgiayonepoly.entites.Material;
import com.example.shopgiayonepoly.service.attribute.MaterialSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attribute")
public class MaterialController {

    @Autowired
    MaterialSevice materialSevice;

    @GetMapping("/material")
    public String list(Model model) {
        model.addAttribute("materialList", materialSevice.findAll());
        model.addAttribute("materialAdd", new Material());
        return "Attribute/material";
    }

    @RequestMapping("/material/add")
    public String add(@ModelAttribute("materialAdd") Material material) {
        material.setStatus(1);
        materialSevice.save(material);
        return "redirect:/attribute/material";
    }


}
