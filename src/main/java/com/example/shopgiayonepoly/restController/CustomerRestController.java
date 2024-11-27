package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.dto.request.CustomerRequest;
import com.example.shopgiayonepoly.dto.response.CustomerResponse;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer-api")
public class CustomerRestController {
    @Autowired
    CustomerService customerService;
    String keyWord = "";
    @GetMapping("/all-customer-status-dislike-0/{page}")
    public List<CustomerResponse> getAllListCustomerDislike0(@PathVariable("page") String page, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        Pageable pageable = PageRequest.of(Integer.parseInt(page)-1, 5);
        Page<CustomerResponse> pageCustomer = customerService.searchCustomerByKeywordPage(keyWord,pageable);
        return pageCustomer.getContent();
    }

    @GetMapping("/max-page-customer-status-dislike-0")
    public Integer getMaxPageCustomerDislike0() {
        System.out.println("so du lieu la " + this.customerService.searchCustomerByKeyword(keyWord).size());
        Integer pageNumber = (int) Math.ceil((double) this.customerService.searchCustomerByKeyword(keyWord).size() / 5);
        return pageNumber;
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchCustomerByKey(@RequestBody Map<String, String> data) {
        String key = data.get("key").trim();
        keyWord = key;
        return ResponseEntity.ok(keyWord);
    }
}
