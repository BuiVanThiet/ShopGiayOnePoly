package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.entites.Staff;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ErrorController {
    @GetMapping("/404")
    public String getForm404() {
        return "Error/404";
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session){
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }
}
