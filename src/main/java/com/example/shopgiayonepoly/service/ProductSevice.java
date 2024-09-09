package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ProductSevice {
    List<Product> findAll();

    <S extends Product> S save(S entity);

    Optional<Product> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    List<Product> findAll(Sort sort);

    Page<Product> findAll(Pageable pageable);


}
