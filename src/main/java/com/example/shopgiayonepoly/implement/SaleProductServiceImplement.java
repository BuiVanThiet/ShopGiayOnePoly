package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.SaleProduct;
import com.example.shopgiayonepoly.repositores.SaleProductRepository;
import com.example.shopgiayonepoly.service.SaleProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SaleProductServiceImplement implements SaleProductService {
    @Autowired
    private SaleProductRepository saleProductRepository;
    @Override
    public Page<SaleProduct> getAllSaleProductByPage(Pageable pageable) {
        return null;
    }

    @Override
    public List<SaleProduct> getAll() {
        return null;
    }

    @Override
    public Page<SaleProduct> getAllSaleProductDeleteByPage(Pageable pageable) {
        return null;
    }

    @Override
    public List<SaleProduct> getAllSaleProductDelete() {
        return null;
    }

    @Override
    public Page<SaleProduct> getSaleProductExpiredByPage(Pageable pageable) {
        return null;
    }

    @Override
    public void updateSaleProductExpired(Integer id) {

    }

    @Override
    public SaleProduct getOne(Integer integer) {
        return null;
    }

    @Override
    public void deleteSaleProduct(Integer id) {

    }

    @Override
    public void restoreStatusSaleProduct(Integer id) {

    }

    @Override
    public Page<SaleProduct> searchSaleProductByKeyword(String key, Pageable pageable) {
        return null;
    }

    @Override
    public Page<SaleProduct> searchSaleProductByTypeSaleProduct(int type, Pageable pageable) {
        return null;
    }

    @Override
    public void updateSaleProductStatusForExpired() {

    }
}
