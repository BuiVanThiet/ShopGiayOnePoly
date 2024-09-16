package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.Product;
import com.example.shopgiayonepoly.entites.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface StaffService {
    List<Staff> findAll();

    <S extends Staff> S save(S entity);

    Optional<Staff> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    List<Staff> findAll(Sort sort);

    Page<Staff> findAll(Pageable pageable);
}
