package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.request.ProductDetailCheckRequest;
import com.example.shopgiayonepoly.entites.BillDetail;
import com.example.shopgiayonepoly.entites.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BillDetailService {
    List<BillDetail> findAll();

    <S extends BillDetail> S save(S entity);

    Optional<BillDetail> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    void delete(BillDetail entity);

    List<BillDetail> findAll(Sort sort);

    Page<BillDetail> findAll(Pageable pageable);

    Page<BillDetail> getBillDetailByIdBill(Integer idBill, Pageable pageable);

    List<ProductDetail> getAllProductDetail();

    ProductDetail getProductDetailById(Integer id);

    Integer getBillDetailExist(Integer idBill, Integer idPDT);

    List<BillDetail> getBillDetailByIdBill(Integer idBill);

    Integer getFirstBillDetailIdByIdBill(Integer idBill);

    Page<ProductDetail> getProductDetailSale(ProductDetailCheckRequest productDetailCheckRequest, Pageable pageable);

    Integer getProductDetailSale(ProductDetailCheckRequest productDetailCheckRequest);

    BigDecimal getTotalAmountByIdBill(Integer id);
}
