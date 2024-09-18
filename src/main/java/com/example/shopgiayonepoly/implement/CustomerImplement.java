package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerImplement implements CustomerService {

    @Override
    public List<Customer> findAll() {
        return null;
    }

    @Override
    public <S extends Customer> S save(S entity) {
        return null;
    }

    @Override
    public Optional<Customer> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public long count() {
        return 0;
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
}
