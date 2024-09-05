package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.BillDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface BillDetailImplement {
    List<BillDetail> findAll();

    <S extends BillDetail> S save(S entity);

    Optional<BillDetail> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    void delete(BillDetail entity);

    List<BillDetail> findAll(Sort sort);

    Page<BillDetail> findAll(Pageable pageable);

    List<BillDetail> getBillDetailByIdBill(Integer idBill, Pageable pageable);
}
