package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.CustomerRequest;
import com.example.shopgiayonepoly.dto.request.StaffRequest;
import com.example.shopgiayonepoly.dto.response.CustomerResponse;
import com.example.shopgiayonepoly.dto.response.StaffResponse;
import com.example.shopgiayonepoly.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping("/list")
    public String list(ModelMap modelMap) {
        List<CustomerResponse> listCustomer = customerService.getAllCustomer();
        modelMap.addAttribute("customerList", customerService.getAllCustomer());
//        System.out.println(customerService.findAll());
        return "Customer/list";
    }

    @GetMapping("/search")
    public String searchCustomerByKey(@RequestParam(name = "key") String key, Model model) {
        List<CustomerResponse> searchCustomer = customerService.searchCustomerByKeyword(key);
        model.addAttribute("customerList", searchCustomer);
        model.addAttribute("customer", new CustomerRequest());
        return "customer/list";
    }
}
