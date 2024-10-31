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

    @Override
    public boolean existsByEmail(String email) {
        return customerRegisterRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByAcount(String acount) {
        return customerRegisterRepository.existsByAcount(acount);
    }

    @Override
    public Customer findByAcount(String acount) {
        return customerRegisterRepository.findByAcount(acount);
    }

    @Override
    public <S extends Customer> S save(S entity) {
        return customerRegisterRepository.save(entity);
    }

}
