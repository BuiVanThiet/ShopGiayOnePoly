package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.CustomerRequest;
import com.example.shopgiayonepoly.dto.request.StaffRequest;
import com.example.shopgiayonepoly.dto.response.CustomerResponse;
import com.example.shopgiayonepoly.dto.response.StaffResponse;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/staff/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping("/list")
    public String getFormList(Model model) {
        List<CustomerResponse> listCustomer = customerService.getAllCustomer();
        model.addAttribute("customerList", customerService.getAllCustomer());
        return "Customer/list";
    }

    @GetMapping("/search")
    public String searchCustomerByKey(@RequestParam(name = "key") String key, Model model) {
        List<CustomerResponse> searchCustomer = customerService.searchCustomerByKeyword(key);
        model.addAttribute("customerList", searchCustomer);
        model.addAttribute("customer", new CustomerRequest());
        return "customer/list";
    }

    @GetMapping("/create")
    public String createCustomer(ModelMap modelMap){
        modelMap.addAttribute("customer",new CustomerRequest());
        return "Customer/create";
    }

    @PostMapping("/add")
    public String addCustomer(Model model, @ModelAttribute(name="customer") CustomerRequest customerRequest){
        System.out.println("Du lieu khi them cua customer: " + customerRequest.toString());
        Customer customer = new Customer();
        customer.setFullName(customerRequest.getFullName());
        customer.setNumberPhone(customerRequest.getNumberPhone());
        customer.setBirthDay(customerRequest.getBirthDay());
        customer.setImage(customerRequest.getNameImage());
        customer.setEmail(customer.getEmail());
        customer.setAcount("");
        customer.setPassword("");
        customer.setGender(customerRequest.getGender());
        customer.setAddRess(customerRequest.getCommune() + "," + customerRequest.getDistrict() + "," + customerRequest.getCity() + "," +customerRequest.getAddRessDetail());
        customer.setStatus(customerRequest.getStatus());
        Customer customerSave = this.customerService.save(customer);
        customerSave.setAcount(customerSave.getFullName()+customerSave.getId());
        customer.setPassword("@shoponepoly");
        this.customerService.save(customerSave);
        return "redirect:/customer/create";
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session){
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }

    @GetMapping("/edit/{id}")
    public String editCustomer(Model model, @PathVariable("id") Integer id) {
        Customer customer = customerService.getOne(id);
        CustomerRequest customerRequest = new CustomerRequest();
        BeanUtils.copyProperties(customer, customerRequest);
        model.addAttribute("customer", customerRequest);
        return "Customer/update";
    }

    @GetMapping("/detail/{id}")
    public String detailCustomer(Model model, @PathVariable("id") Integer id) {
        Customer customer = customerService.getOne(id);
        CustomerRequest customerRequest = new CustomerRequest();
        BeanUtils.copyProperties(customer, customerRequest);
        model.addAttribute("customer", customerRequest);
        return "Customer/detail";
    }
}
