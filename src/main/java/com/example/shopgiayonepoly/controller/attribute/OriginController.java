package com.example.shopgiayonepoly.controller.attribute;

import com.example.shopgiayonepoly.entites.Origin;
import com.example.shopgiayonepoly.service.attribute.OriginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attribute")
public class OriginController {

    @Autowired
    OriginService originSevice;

    @GetMapping("/origin")
    public String list(Model model) {
        model.addAttribute("originList", originSevice.findAll());
        model.addAttribute("originAdd" , new Origin());
        return "Attribute/origin";
    }

    @RequestMapping("/origin/add")
    public String add(@ModelAttribute("originAdd") Origin origin){
        origin.setStatus(1);
        originSevice.save(origin);
        return "redirect:/attribute/origin";
    }



}
