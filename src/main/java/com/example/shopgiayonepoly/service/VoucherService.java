package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.request.VoucherRequest;
import com.example.shopgiayonepoly.entites.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VoucherService {
    public Page<Voucher> getAllVoucherByPage(Pageable pageable);
    public List<Voucher> getAll();
    public void  createNewVoucher(VoucherRequest voucherRequest);
    public void updateVoucher(VoucherRequest voucherRequest);
    public Voucher getOne(Integer integer);
    public void deleteVoucher(Integer id);
}
