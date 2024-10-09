package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.ReturnBillDetail;
import com.example.shopgiayonepoly.repositores.ReturnBillDetailReponsetory;
import com.example.shopgiayonepoly.service.ReturnBillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReturnBillDetailImplement implements ReturnBillDetailService {
    @Autowired
    ReturnBillDetailReponsetory returnBillDetailReponsetory;

    @Override
    public <S extends ReturnBillDetail> List<S> saveAll(Iterable<S> entities) {
        return returnBillDetailReponsetory.saveAll(entities);
    }

    @Override
    public List<ReturnBillDetail> findAll() {
        return returnBillDetailReponsetory.findAll();
    }

    @Override
    public <S extends ReturnBillDetail> S save(S entity) {
        return returnBillDetailReponsetory.save(entity);
    }

    @Override
    public Optional<ReturnBillDetail> findById(Integer integer) {
        return returnBillDetailReponsetory.findById(integer);
    }

    @Override
    public long count() {
        return returnBillDetailReponsetory.count();
    }

    @Override
    public void deleteById(Integer integer) {
        returnBillDetailReponsetory.deleteById(integer);
    }

    @Override
    public void delete(ReturnBillDetail entity) {
        returnBillDetailReponsetory.delete(entity);
    }

    @Override
    public List<ReturnBillDetail> findAll(Sort sort) {
        return returnBillDetailReponsetory.findAll(sort);
    }

    @Override
    public Page<ReturnBillDetail> findAll(Pageable pageable) {
        return returnBillDetailReponsetory.findAll(pageable);
    }
    @Override
    public Page<ReturnBillDetail> getReturnBillDetailByIdReturnBill(Integer idReturnBill, Pageable pageable) {
        return this.returnBillDetailReponsetory.getReturnBillDetailByIdReturnBill(idReturnBill, pageable);
    }
    @Override
    public List<ReturnBillDetail> getReturnBillDetailByIdReturnBill(Integer idReturnBill) {
        return this.returnBillDetailReponsetory.getReturnBillDetailByIdReturnBill(idReturnBill);
    }

}
