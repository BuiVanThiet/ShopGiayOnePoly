package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.PaymentExchange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface PaymentExchangeService {
    <S extends PaymentExchange> List<S> saveAll(Iterable<S> entities);

    List<PaymentExchange> findAll();

    <S extends PaymentExchange> S save(S entity);

    Optional<PaymentExchange> findById(Integer integer);

    void deleteById(Integer integer);

    void delete(PaymentExchange entity);

    List<PaymentExchange> findAll(Sort sort);

    Page<PaymentExchange> findAll(Pageable pageable);

    List<PaymentExchange> getPaymentExchangeByIdBillExchange(Integer id);
}
