package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.Bill;
import com.example.shopgiayonepoly.implement.BillImplement;
import com.example.shopgiayonepoly.repositores.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillService implements BillImplement {
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
}
