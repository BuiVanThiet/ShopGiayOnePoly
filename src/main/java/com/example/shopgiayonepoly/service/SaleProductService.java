package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.SaleProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SaleProductService {
    public Page<SaleProduct> getAllSaleProductByPage(Pageable pageable);

    public List<SaleProduct> getAll();

    public Page<SaleProduct> getAllSaleProductDeleteByPage(Pageable pageable);

    public List<SaleProduct> getAllSaleProductDelete();

    public Page<SaleProduct> getSaleProductExpiredByPage(Pageable pageable);
    public void updateSaleProductExpired(@Param("id") Integer id);

//    public void createNewSaleProduct(SaleProductRequest voucherRequest);
//
//    public void updateSaleProduct(SaleProductRequest voucherRequest);

    public SaleProduct getOne(Integer integer);

    public void deleteSaleProduct(Integer id);

    public void restoreStatusSaleProduct(Integer id);

    public Page<SaleProduct> searchSaleProductByKeyword(String key, Pageable pageable);
    Page<SaleProduct> searchSaleProductByTypeSaleProduct(@Param("types") int type, Pageable pageable);

    public void updateSaleProductStatusForExpired();


}
