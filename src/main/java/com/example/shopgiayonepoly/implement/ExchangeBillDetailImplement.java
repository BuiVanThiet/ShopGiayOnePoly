package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.ExchangeBillDetail;
import com.example.shopgiayonepoly.repositores.ExchangeBillDetailRepository;
import com.example.shopgiayonepoly.service.ExchangeBillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ExchangeBillDetailImplement implements ExchangeBillDetailService {
    @Autowired
    ExchangeBillDetailRepository exchangeBillDetailRepository;

    @Override
    public List<ExchangeBillDetail> findAll() {
        return exchangeBillDetailRepository.findAll();
    }

    @Override
    public <S extends ExchangeBillDetail> S save(S entity) {
        return exchangeBillDetailRepository.save(entity);
    }

    @Override
    public Optional<ExchangeBillDetail> findById(Integer integer) {
        return exchangeBillDetailRepository.findById(integer);
    }

    @Override
    public long count() {
        return exchangeBillDetailRepository.count();
    }

    @Override
    public void deleteById(Integer integer) {
        exchangeBillDetailRepository.deleteById(integer);
    }

    @Override
    public void delete(ExchangeBillDetail entity) {
        exchangeBillDetailRepository.delete(entity);
    }

    @Override
    public List<ExchangeBillDetail> findAll(Sort sort) {
        return exchangeBillDetailRepository.findAll(sort);
    }

    @Override
    public Page<ExchangeBillDetail> findAll(Pageable pageable) {
        return exchangeBillDetailRepository.findAll(pageable);
    }
    @Override
    public Page<ExchangeBillDetail> getExchangeBillDetailByIdReturnBill(Integer idExchangeBill, Pageable pageable) {
        return exchangeBillDetailRepository.getExchangeBillDetailByIdReturnBill(idExchangeBill,pageable);
    }
    @Override
    public List<ExchangeBillDetail> getExchangeBillDetailByIdReturnBill(Integer idExchangeBill) {
        return exchangeBillDetailRepository.getExchangeBillDetailByIdReturnBill(idExchangeBill);
    }

}
