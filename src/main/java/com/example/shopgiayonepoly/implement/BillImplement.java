package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.response.BillTotalInfornationResponse;
import com.example.shopgiayonepoly.entites.Bill;
import com.example.shopgiayonepoly.entites.Client;
import com.example.shopgiayonepoly.repositores.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillImplement implements com.example.shopgiayonepoly.service.BillService {
    @Autowired
    BillRepository billRepository;

    @Override
    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    @Override
    public <S extends Bill> S save(S entity) {
        return billRepository.save(entity);
    }

    @Override
    public Optional<Bill> findById(Integer integer) {
        return billRepository.findById(integer);
    }

    @Override
    public long count() {
        return billRepository.count();
    }

    @Override
    public void deleteById(Integer integer) {
        billRepository.deleteById(integer);
    }

    @Override
    public List<Bill> findAll(Sort sort) {
        return billRepository.findAll(sort);
    }

    @Override
    public Page<Bill> findAll(Pageable pageable) {
        return billRepository.findAll(pageable);
    }
    @Override
    public List<Bill> getBillByStatusNew(Pageable pageable) {
        return billRepository.getBillByStatusNew(pageable);
    }

    @Override
    public List<Client> getClientNotStatus0() {
        return this.billRepository.getClientNotStatus0();
    }

    @Override
    public BillTotalInfornationResponse findBillVoucherById(Integer id) {
        return this.billRepository.findBillVoucherById(id);
    }
}
