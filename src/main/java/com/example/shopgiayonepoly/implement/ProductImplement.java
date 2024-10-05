package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.Product;
import com.example.shopgiayonepoly.repositores.ProductRepository;
import com.example.shopgiayonepoly.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductImplement implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public <S extends Product> S save(S entity) {
        return productRepository.save(entity);
    }

    @Override
    public Optional<Product> findById(Integer integer) {
        return productRepository.findById(integer);
    }

    @Override
    public long count() {
        return productRepository.count();
    }

    @Override
    public List<Product> findAll(Sort sort) {
        return productRepository.findAll(sort);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> getProductNotStatus0() {
        return this.productRepository.getProductNotStatus0();
    }

    @Override
    public List<Product> getProductDelete() {
        return this.productRepository.getProductDelete();
    }

    @Override
    public void updateStatus(int id, int status) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setStatus(status);
            productRepository.save(product);
        }
    }

    @Override
    public void deleteByID(int id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setStatus(0);
            productRepository.save(product);
        }
    }

    @Override
    public void updateProduct(int id, String codeProduct, String nameProduct) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setCodeProduct(codeProduct);
            product.setNameProduct(nameProduct);
            productRepository.save(product);
        } else {
            throw new RuntimeException("Màu sắc có " + id + " Không tồn tại.");
        }
    }

    public Optional<Product> getOneProductByCodeProduct(String codeProduct){
        return productRepository.getOneProductByCodeProduct(codeProduct);
    }


}
