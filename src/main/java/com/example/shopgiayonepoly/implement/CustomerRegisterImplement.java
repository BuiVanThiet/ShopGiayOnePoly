package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.request.RegisterRequest;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.repositores.CustomerRegisterRepository;
import com.example.shopgiayonepoly.service.CustomerRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerRegisterImplement implements CustomerRegisterService {
    @Autowired
    CustomerRegisterRepository customerRegisterRepository;

    public String registerCustomer(Customer customer) {
        customerRegisterRepository.save(customer);
        return "Đăng ký thành công!";
    }

}
