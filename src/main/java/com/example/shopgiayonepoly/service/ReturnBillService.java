package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.ReturnBillExchangeBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ReturnBillService {

    List<ReturnBillExchangeBill> findAll();

    <S extends ReturnBillExchangeBill> S save(S entity);

    Optional<ReturnBillExchangeBill> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    void delete(ReturnBillExchangeBill entity);

    List<ReturnBillExchangeBill> findAll(Sort sort);

    Page<ReturnBillExchangeBill> findAll(Pageable pageable);

    ReturnBillExchangeBill getReturnBillByIdBill(Integer idBill);
}
