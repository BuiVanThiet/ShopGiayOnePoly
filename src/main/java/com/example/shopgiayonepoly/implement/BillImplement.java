package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface BillImplement {
    List<Bill> findAll();

    <S extends Bill> S save(S entity);

    Optional<Bill> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    List<Bill> findAll(Sort sort);

    Page<Bill> findAll(Pageable pageable);
}
