package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.ShiftService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff/shift")
public class ShiftController {
    @Autowired
    ShiftService shiftService;
    @GetMapping("/home")
    public String getFormHomeShift(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return "redirect:/login";
        }
        if(staffLogin.getStatus() != 1) {
            return "redirect:/home_manage";
        }
        return "shift/home";
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }
}
