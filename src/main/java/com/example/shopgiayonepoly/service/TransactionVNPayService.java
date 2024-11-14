package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.request.TransactionCheckRequest;
import com.example.shopgiayonepoly.entites.TransactionVNPay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TransactionVNPayService {
    List<TransactionVNPay> findAll();

    <S extends TransactionVNPay> S save(S entity);

    Optional<TransactionVNPay> findById(Integer integer);

    void deleteById(Integer integer);

    Page<TransactionVNPay> findAll(Pageable pageable);

    List<Object[]> getAllTransactionVNPay(
            TransactionCheckRequest transactionCheckRequest
    );

    List<Object[]> getTransactionById(Integer id);
}
