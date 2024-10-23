package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.ReturnBillExchangeBill;
import com.example.shopgiayonepoly.repositores.ReturnBillReponsetory;
import com.example.shopgiayonepoly.service.ReturnBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReturnBillImplement implements ReturnBillService {
    @Autowired
    ReturnBillReponsetory returnBillReponsetory;

    @Override
    public List<ReturnBillExchangeBill> findAll() {
        return returnBillReponsetory.findAll();
    }

    @Override
    public <S extends ReturnBillExchangeBill> S save(S entity) {
        return returnBillReponsetory.save(entity);
    }

    @Override
    public Optional<ReturnBillExchangeBill> findById(Integer integer) {
        return returnBillReponsetory.findById(integer);
    }

    @Override
    public long count() {
        return returnBillReponsetory.count();
    }

    @Override
    public void deleteById(Integer integer) {
        returnBillReponsetory.deleteById(integer);
    }

    @Override
    public void delete(ReturnBillExchangeBill entity) {
        returnBillReponsetory.delete(entity);
    }

    @Override
    public List<ReturnBillExchangeBill> findAll(Sort sort) {
        return returnBillReponsetory.findAll(sort);
    }

    @Override
    public Page<ReturnBillExchangeBill> findAll(Pageable pageable) {
        return returnBillReponsetory.findAll(pageable);
    }
    @Override
    public ReturnBillExchangeBill getReturnBillByIdBill(Integer idBill) {
        return this.returnBillReponsetory.getReturnBillByIdBill(idBill);
    }
}
