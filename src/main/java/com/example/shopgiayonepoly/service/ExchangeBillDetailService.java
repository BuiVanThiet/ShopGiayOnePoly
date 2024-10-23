package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.ExchangeBillDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ExchangeBillDetailService {
    List<ExchangeBillDetail> findAll();

    <S extends ExchangeBillDetail> S save(S entity);

    Optional<ExchangeBillDetail> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    void delete(ExchangeBillDetail entity);

    List<ExchangeBillDetail> findAll(Sort sort);

    Page<ExchangeBillDetail> findAll(Pageable pageable);

    Page<ExchangeBillDetail> getExchangeBillDetailByIdReturnBill(Integer idExchangeBill, Pageable pageable);

    List<ExchangeBillDetail> getExchangeBillDetailByIdReturnBill(Integer idExchangeBill);
}
