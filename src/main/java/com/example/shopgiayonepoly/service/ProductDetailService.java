package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ProductDetailService {
    List<ProductDetail> findAll();

    <S extends ProductDetail> S save(S entity);

    Optional<ProductDetail> findById(Integer integer);

    long count();

    void deleteByID(int id);

    List<ProductDetail> findAll(Sort sort);

    Page<ProductDetail> findAll(Pageable pageable);

    List<ProductDetail> getProductDetailNotStatus0();

    List<ProductDetail> getProductDetailDelete();

    void updateStatus(int id, int status);

    void updateProductDetail(int id, String codeProductDetail, String nameProductDetail);

}
