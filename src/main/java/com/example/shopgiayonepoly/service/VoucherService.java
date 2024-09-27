package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.request.VoucherRequest;
import com.example.shopgiayonepoly.entites.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface VoucherService {
    public Page<Voucher> getAllVoucherByPage(Pageable pageable);

    public List<Voucher> getAll();

    public Page<Voucher> getAllVoucherDeleteByPage(Pageable pageable);

    public List<Voucher> getAllVoucherDelete();

    public void createNewVoucher(VoucherRequest voucherRequest);

    public void updateVoucher(VoucherRequest voucherRequest);

    public Voucher getOne(Integer integer);

    public void deleteVoucher(Integer id);

    public void restoreStatusVoucher(Integer id);

    public Page<Voucher> searchVoucherByKeyword(String key, Pageable pageable);
    public Page<Voucher> searchVoucherByDateRange(Pageable pageable, LocalDate startDate, LocalDate endDate);
    public Page<Voucher> searchVoucherByPriceRange(Pageable pageable, BigDecimal minPrice, BigDecimal maxPrice);
    Page<Voucher> searchVoucherByTypeVoucher(@Param("types") int type, Pageable pageable);


}
