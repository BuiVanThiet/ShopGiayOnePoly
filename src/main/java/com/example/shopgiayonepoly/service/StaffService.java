package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.request.StaffRequest;
import com.example.shopgiayonepoly.dto.request.VoucherRequest;
import com.example.shopgiayonepoly.dto.response.StaffResponse;
import com.example.shopgiayonepoly.entites.Product;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.entites.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StaffService {
    List<StaffResponse> getAllStaff();

    public List<StaffResponse> searchStaffByKeyword(String key);

    <S extends Staff> S save(S entity);

    Optional<Staff> findById(Integer integer);

    long count();

    public void updateStaff(StaffRequest staffRequest);

    public Staff getOne(Integer integer);

    void deleteById(Integer integer);

    List<Staff> findAll(Sort sort);

    Page<Staff> findAll(Pageable pageable);
}
