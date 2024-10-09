package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.ReturnBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ReturnBillService {

    List<ReturnBill> findAll();

    <S extends ReturnBill> S save(S entity);

    Optional<ReturnBill> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    void delete(ReturnBill entity);

    List<ReturnBill> findAll(Sort sort);

    Page<ReturnBill> findAll(Pageable pageable);

    ReturnBill getReturnBillByIdBill(Integer idBill);
}
