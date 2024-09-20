package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping("/list")
    public String list(ModelMap modelMap, Model model) {
        model.addAttribute("customerList", customerService.findAll());
        System.out.println(customerService.findAll());
        return "Customer/list";
    }
}
