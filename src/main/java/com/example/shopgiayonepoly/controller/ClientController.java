package com.example.shopgiayonepoly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/onepoly")
public class ClientController {
    @GetMapping("/home")
    public String getFormHomeClient(){
        return "client/homepage";
    }
    @GetMapping("/products")
    public String getFormProduct(){
        return "client/product";
    }
    @GetMapping("/product_detail")
    public String testMenu(){
        return "client/product_detail";
    }
}
