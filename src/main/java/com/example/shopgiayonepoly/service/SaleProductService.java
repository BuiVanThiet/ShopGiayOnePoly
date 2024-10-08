package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.request.SaleProductRequest;
import com.example.shopgiayonepoly.dto.request.VoucherRequest;
import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.entites.SaleProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SaleProductService {
    public Page<SaleProduct> getAllSaleProductByPage(Pageable pageable);
    public List<SaleProduct> getAllSaleProducts();
    public Page<SaleProduct> getDeletedSaleProductsByPage(Pageable pageable);
    public List<SaleProduct> getAllDeletedSaleProducts();
    public void deleteSaleProductBySetStatus(Integer id);
    public void restoreSaleProductStatus(Integer id);
    public Page<SaleProduct> searchSaleProductsByKeyword(String key, Pageable pageable);
    public Page<SaleProduct> searchSaleProductsByType(int type, Pageable pageable);
    public List<ProductDetail> getAllProductDetailByPage();
    public void createNewSale(SaleProductRequest saleProductRequest);
    public SaleProduct getSaleProductByID(Integer id);
    public void applyDiscountToProductDetails(List<Integer> productIds, BigDecimal discountValue, Integer discountType, Integer saleProductId);
}

