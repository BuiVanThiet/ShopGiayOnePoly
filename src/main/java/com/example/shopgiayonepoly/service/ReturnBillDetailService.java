package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.ReturnBillDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ReturnBillDetailService {

    <S extends ReturnBillDetail> List<S> saveAll(Iterable<S> entities);

    List<ReturnBillDetail> findAll();

    <S extends ReturnBillDetail> S save(S entity);

    Optional<ReturnBillDetail> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    void delete(ReturnBillDetail entity);

    List<ReturnBillDetail> findAll(Sort sort);

    Page<ReturnBillDetail> findAll(Pageable pageable);

    Page<ReturnBillDetail> getReturnBillDetailByIdReturnBill(Integer idReturnBill, Pageable pageable);

    List<ReturnBillDetail> getReturnBillDetailByIdReturnBill(Integer idReturnBill);
}
