package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.TransactionVNPayService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/staff/transactionVNPay")
public class TransactionVNPayController {
    @Autowired
    TransactionVNPayService transactionVNPayService;
    @GetMapping("/home")
    public String getFormTransactionVNPay(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return "redirect:/login";
        }
        return "transactionVNPay/transactionVNPayHome";
    }
    @GetMapping("/transaction-by-id/{id}")
    public String getFormTransactionVNPay(@PathVariable("id") String id, HttpSession session, ModelMap modelMap) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return "redirect:/login";
        }
        try {
            Integer idNumber = Integer.parseInt(id);
            List<Object[]> transactionById = this.transactionVNPayService.getTransactionById(idNumber);
            modelMap.addAttribute("transaction",transactionById.get(0));
            return "transactionVNPay/transactionVNPayDetail";
        }catch (NumberFormatException e) {
            return "redirect:/404";
        }
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session){
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }
}
