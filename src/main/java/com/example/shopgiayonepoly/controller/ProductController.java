package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.entites.Product;
import com.example.shopgiayonepoly.repositores.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductRepository productRepository;



    @GetMapping("/list")
    public String list(ModelMap modelMap, Model model) {
//        modelMap.addAttribute("list","/Product/list");
        model.addAttribute("productList", productRepository.findAll());
        System.out.println(productRepository.findAll());
        return "Product/list";
    }

    @GetMapping("/create")
    public String create(ModelMap modelMap) {
        modelMap.addAttribute("page","/Product/create");
        return "Product/create";
    }

}
