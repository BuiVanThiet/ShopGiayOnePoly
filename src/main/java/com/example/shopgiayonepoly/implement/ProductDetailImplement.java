package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.repositores.ProductDetailRepository;
import com.example.shopgiayonepoly.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductDetailImplement implements ProductDetailService {
    @Autowired
    ProductDetailRepository product_detailRepository;

    @Override
    public List<ProductDetail> findAll() {
        return product_detailRepository.findAll();
    }

    @Override
    public <S extends ProductDetail> S save(S entity) {
        return product_detailRepository.save(entity);
    }

    @Override
    public Optional<ProductDetail> findById(Integer integer) {
        return product_detailRepository.findById(integer);
    }

    @Override
    public long count() {
        return product_detailRepository.count();
    }

    @Override
    public List<ProductDetail> findAll(Sort sort) {
        return product_detailRepository.findAll(sort);
    }

    @Override
    public Page<ProductDetail> findAll(Pageable pageable) {
        return product_detailRepository.findAll(pageable);
    }

    @Override
    public List<ProductDetail> getProductDetailNotStatus0() {
        return this.product_detailRepository.getProductDetailNotStatus0();
    }

    @Override
    public List<ProductDetail> getProductDetailDelete() {
        return this.product_detailRepository.getProductDetailDelete();
    }

    @Override
    public void updateStatus(int id, int status) {
        Optional<ProductDetail> optionalProductDetail = product_detailRepository.findById(id);
        if (optionalProductDetail.isPresent()) {
            ProductDetail product_detail = optionalProductDetail.get();
            product_detail.setStatus(status);
            product_detailRepository.save(product_detail);
        }
    }

    @Override
    public void deleteByID(int id) {
        Optional<ProductDetail> optionalProductDetail = product_detailRepository.findById(id);
        if (optionalProductDetail.isPresent()) {
            ProductDetail product_detail = optionalProductDetail.get();
            product_detail.setStatus(0);
            product_detailRepository.save(product_detail);
        }
    }

    @Override
    public void updateProductDetail(int id, String codeProductDetail, String nameProductDetail) {
        Optional<ProductDetail> optionalProductDetail = product_detailRepository.findById(id);

        if (optionalProductDetail.isPresent()) {
            ProductDetail product_detail = optionalProductDetail.get();
            product_detailRepository.save(product_detail);
        } else {
            throw new RuntimeException("Màu sắc có " + id + " Không tồn tại.");
        }
    }


}
