package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();

    <S extends Product> S save(S entity);

    Optional<Product> findById(Integer integer);

    long count();

    void deleteByID(int id);

    List<Product> findAll(Sort sort);

    Page<Product> findAll(Pageable pageable);

    List<Product> getProductNotStatus0();

    List<Product> getProductDelete();

    void updateStatus(int id, int status);

    void updateProduct(int id, String codeProduct, String nameProduct);

    Optional<Product> getOneProductByCodeProduct(String codeProduct);

}
