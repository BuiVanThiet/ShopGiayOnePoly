package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.response.CustomerResponse;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.repositores.CustomerRepository;
import com.example.shopgiayonepoly.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerImplement implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Override
    public List<CustomerResponse> getAllCustomer() {
        return customerRepository.getAllCustomer();
    }

    @Override
    public Page<Customer> getAllCustomerByPage(Pageable pageable) {
        return customerRepository.getAllCustomrByPage(pageable);
    }

    @Override
    public List<CustomerResponse> searchCustomerByKeyword(String key) {
        return customerRepository.searchCustomerByKeyword(key);
    }

    @Override
    public <S extends Customer> S save(S entity) {
        return customerRepository.save(entity);
    }

    @Override
    public Optional<Customer> findById(Integer integer) {
        return customerRepository.findById(integer);
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public Customer getOne(Integer integer) {
        return customerRepository.findById(integer).get();
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public List<Customer> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public void deleteCustomer(Integer id) {
        customerRepository.deleteBySetStatus(id);
    }
}
