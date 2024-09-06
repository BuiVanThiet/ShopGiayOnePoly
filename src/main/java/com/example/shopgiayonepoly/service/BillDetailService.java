package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.BillDetail;
import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.implement.BillDetailImplement;
import com.example.shopgiayonepoly.repositores.BillDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillDetailService implements BillDetailImplement {
    @Autowired
    BillDetailRepository billDetailRepository;

    @Override
    public List<BillDetail> findAll() {
        return billDetailRepository.findAll();
    }

    @Override
    public <S extends BillDetail> S save(S entity) {
        return billDetailRepository.save(entity);
    }

    @Override
    public Optional<BillDetail> findById(Integer integer) {
        return billDetailRepository.findById(integer);
    }

    @Override
    public long count() {
        return billDetailRepository.count();
    }

    @Override
    public void deleteById(Integer integer) {
        billDetailRepository.deleteById(integer);
    }

    @Override
    public void delete(BillDetail entity) {
        billDetailRepository.delete(entity);
    }

    @Override
    public List<BillDetail> findAll(Sort sort) {
        return billDetailRepository.findAll(sort);
    }

    @Override
    public Page<BillDetail> findAll(Pageable pageable) {
        return billDetailRepository.findAll(pageable);
    }
    @Override
    public List<BillDetail> getBillDetailByIdBill(Integer idBill, Pageable pageable) {
        return billDetailRepository.getBillDetailByIdBill(idBill,pageable);
    }

    @Override
    public List<ProductDetail> getAllProductDetail() {
        return this.billDetailRepository.getAllProductDetail();
    }
    @Override
    public ProductDetail getProductDetailById(Integer id) {
        return this.billDetailRepository.getProductDetailById(id);
    }

}
