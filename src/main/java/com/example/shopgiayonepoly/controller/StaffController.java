package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    StaffService staffService;

    @GetMapping("/list")
    public String list(ModelMap modelMap, Model model) {
        model.addAttribute("staffList", staffService.findAll());
        System.out.println(staffService.findAll());
        return "Staff/list";
    }

}
