package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.entites.SaleProduct;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.SaleProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff/sale-product")
public class SaleProductController {
    @Autowired
    private SaleProductService saleProductService;
    @GetMapping("/list")
    public String getFormListSaleProduct(Model model){
        SaleProduct saleProduct = new SaleProduct();
        model.addAttribute("saleProduct",saleProduct);
        return "sale-product/index";
    }
    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }
}
