package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.request.VoucherRequest;
import com.example.shopgiayonepoly.entites.Voucher;
import com.example.shopgiayonepoly.repositores.VoucherRepository;
import com.example.shopgiayonepoly.service.VoucherService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class VoucherServiceImplement implements VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    public Page<Voucher> getAllVoucherByPage(Pageable pageable) {
        return voucherRepository.getAllVoucherByPage(pageable);
    }

    @Override
    public List<Voucher> getAll() {
        return voucherRepository.getAllVoucher();
    }

    @Override
    public Page<Voucher> getAllVoucherDeleteByPage(Pageable pageable) {
        return voucherRepository.getVoucherDeleteByPage(pageable);
    }

    @Override
    public List<Voucher> getAllVoucherDelete() {
        return voucherRepository.getAllVoucherDelete();
    }

    @Override
    public Page<Voucher> getVoucherExpiredByPage(Pageable pageable) {
        return  voucherRepository.getVoucherExpiredByPage(pageable);
    }

    @Override
    public void updateVoucherExpired(Integer id) {
        voucherRepository.updateVoucherExpired(id);
    }


    @Override
    public void createNewVoucher(VoucherRequest voucherRequest) {
        Voucher voucher = new Voucher();
        BeanUtils.copyProperties(voucherRequest, voucher);
        voucherRepository.save(voucher);
    }

    @Override
    public void updateVoucher(VoucherRequest voucherRequest) {
        Voucher voucher = new Voucher();
        BeanUtils.copyProperties(voucherRequest, voucher);
        voucherRepository.save(voucher);
    }

    @Override
    public Voucher getOne(Integer integer) {
        return voucherRepository.findById(integer).get();
    }

    @Override
    public void deleteVoucher(Integer id) {
        voucherRepository.deleteBySetStatus(id);
    }

    @Override
    public void restoreStatusVoucher(Integer id) {
        voucherRepository.restoreStatusVoucher(id);
    }

    @Override
    public Page<Voucher> searchVoucherByKeyword(String key, Pageable pageable) {
        return voucherRepository.searchVoucherByKeyword(key, pageable);
    }

    @Override
    public Page<Voucher> searchVoucherByTypeVoucher(int type, Pageable pageable) {
        return voucherRepository.searchVoucherByTypeVoucher(type,pageable);
    }

    @Override
    public void updateVoucherStatusForExpired() {
        voucherRepository.updateVoucherStatusForExpired();
    }

}
