package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.PaymentExchange;
import com.example.shopgiayonepoly.repositores.PaymentExchangeReposetory;
import com.example.shopgiayonepoly.service.PaymentExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentExchangeImplement implements PaymentExchangeService {
    @Autowired
    PaymentExchangeReposetory paymentExchangeReposetory;

    @Override
    public <S extends PaymentExchange> List<S> saveAll(Iterable<S> entities) {
        return paymentExchangeReposetory.saveAll(entities);
    }

    @Override
    public List<PaymentExchange> findAll() {
        return paymentExchangeReposetory.findAll();
    }

    @Override
    public <S extends PaymentExchange> S save(S entity) {
        return paymentExchangeReposetory.save(entity);
    }

    @Override
    public Optional<PaymentExchange> findById(Integer integer) {
        return paymentExchangeReposetory.findById(integer);
    }

    @Override
    public void deleteById(Integer integer) {
        paymentExchangeReposetory.deleteById(integer);
    }

    @Override
    public void delete(PaymentExchange entity) {
        paymentExchangeReposetory.delete(entity);
    }

    @Override
    public List<PaymentExchange> findAll(Sort sort) {
        return paymentExchangeReposetory.findAll(sort);
    }

    @Override
    public Page<PaymentExchange> findAll(Pageable pageable) {
        return paymentExchangeReposetory.findAll(pageable);
    }
    @Override
    public List<PaymentExchange> getPaymentExchangeByIdBillExchange(Integer id) {
        return this.paymentExchangeReposetory.getPaymentExchangeByIdBillExchange(id);
    }
}
