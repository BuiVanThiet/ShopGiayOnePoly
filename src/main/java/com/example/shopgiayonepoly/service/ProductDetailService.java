package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.implement.ProductDetailImplement;
import com.example.shopgiayonepoly.repositores.ProductDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductDetailService implements ProductDetailImplement {
    @Autowired
    ProductDetailRepository productDetailRepository;

    @Override
    public List<ProductDetail> findAll() {
        return productDetailRepository.findAll();
    }

    @Override
    public <S extends ProductDetail> S save(S entity) {
        return productDetailRepository.save(entity);
    }

    @Override
    public Optional<ProductDetail> findById(Integer integer) {
        return productDetailRepository.findById(integer);
    }

    @Override
    public long count() {
        return productDetailRepository.count();
    }

    @Override
    public void deleteById(Integer integer) {
        productDetailRepository.deleteById(integer);
    }

    @Override
    public List<ProductDetail> findAll(Sort sort) {
        return productDetailRepository.findAll(sort);
    }

    @Override
    public Page<ProductDetail> findAll(Pageable pageable) {
        return productDetailRepository.findAll(pageable);
    }
}
