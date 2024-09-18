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
import org.springframework.stereotype.Service;

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
    public void deleteVoucher(Integer id) {
        voucherRepository.deleteBySetStatus(id);
    }
}
