package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.implement.BillImplement;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/bill")
public class BillController {
    @Autowired
    BillImplement billImplement;
    @GetMapping("/")
    public String getForm(ModelMap modelMap) {
        modelMap.addAttribute("page","/Bill/index");
        return "Home/home";
    }
    @GetMapping("/bill-detail/{idBill}")
    public String getBillDetail(@PathVariable("idBill") Integer idBill, ModelMap modelMap, HttpSession session) {
        if (idBill != null) {
            session.setAttribute("IdBill", idBill);
        } else {
            System.out.println("idBill là null, không thể lưu vào session.");
        }
        modelMap.addAttribute("page","/Bill/index");
        return "Home/home";
    }
}
