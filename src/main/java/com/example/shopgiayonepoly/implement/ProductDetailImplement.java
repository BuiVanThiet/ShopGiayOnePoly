package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ProductDetailImplement {
    List<ProductDetail> findAll();

    <S extends ProductDetail> S save(S entity);

    Optional<ProductDetail> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    List<ProductDetail> findAll(Sort sort);

    Page<ProductDetail> findAll(Pageable pageable);
}
