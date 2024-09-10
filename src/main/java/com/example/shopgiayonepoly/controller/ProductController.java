package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.service.ProductSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductSevice productSevice;

    @GetMapping("/list")
    public String list(ModelMap modelMap, Model model) {
//        modelMap.addAttribute("list","/Product/list");
        model.addAttribute("productList", productSevice.findAll());
        System.out.println(productSevice.findAll());
        return "Product/list";
    }

    @GetMapping("/create")
    public String create(ModelMap modelMap) {
        modelMap.addAttribute("page", "/Product/create");
        return "Product/create";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("deleteByID", "/Product/create");
        productSevice.deleteById(id);
        return "Product/list";
    }

}
