package com.example.shopgiayonepoly.controller.attribute;

import com.example.shopgiayonepoly.entites.Size;
import com.example.shopgiayonepoly.service.attribute.SizeSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attribute")
public class SizeController {

    @Autowired
    SizeSevice sizeSevice;

    @GetMapping("/size")
    public String list(Model model) {
        model.addAttribute("sizeList", sizeSevice.findAll());
        model.addAttribute("sizeAdd" , new Size());
        return "Attribute/size";
    }

    @RequestMapping("/size/add")
    public String add(@ModelAttribute("sizeAdd") Size size){
        size.setStatus(1);
        sizeSevice.save(size);
        return "redirect:/attribute/size";
    }



}
