package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> findAll();

    <S extends Customer> S save(S entity);

    Optional<Customer> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    List<Customer> findAll(Sort sort);

    Page<Customer> findAll(Pageable pageable);
}
