package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.request.StaffRequest;
import com.example.shopgiayonepoly.dto.response.StaffResponse;
import com.example.shopgiayonepoly.entites.SaleProduct;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.entites.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface StaffService {
    List<StaffResponse> getAllStaff();

    public Page<StaffResponse> getAllStaffByPage(Pageable pageable, Integer id);

    public List<StaffResponse> searchStaffByKeyword(String key, Integer id);

    public Page<StaffResponse> searchStaffByKeywordPage(String key, Pageable pageable,Integer id);

    <S extends Staff> S save(S entity);

    Optional<Staff> findById(Integer integer);

    long count();

    public Staff getStaffByID(Integer id);

    public Staff getOne(Integer integer);

    void deleteById(Integer integer);

    List<Staff> findAll(Sort sort);

    Page<Staff> findAll(Pageable pageable);

    public void deleteStaff(Integer id);

    String uploadFile(MultipartFile multipartFile,Integer idStaff) throws IOException;

    public boolean existsByCodeStaff(String codeStaff);

    public Staff existsByEmail(String email);
}
