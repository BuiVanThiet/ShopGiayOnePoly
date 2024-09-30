package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.InvoiceStatus;
import com.example.shopgiayonepoly.repositores.InvoiceStatusRespository;
import com.example.shopgiayonepoly.service.InvoiceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceStatusImplement implements InvoiceStatusService {
    @Autowired
    InvoiceStatusRespository invoiceStatusRespository;

    @Override
    public List<InvoiceStatus> findAll() {
        return invoiceStatusRespository.findAll();
    }

    @Override
    public <S extends InvoiceStatus> S save(S entity) {
        return invoiceStatusRespository.save(entity);
    }

    @Override
    public Optional<InvoiceStatus> findById(Integer integer) {
        return invoiceStatusRespository.findById(integer);
    }

    @Override
    public void deleteById(Integer integer) {
        invoiceStatusRespository.deleteById(integer);
    }

    @Override
    public void delete(InvoiceStatus entity) {
        invoiceStatusRespository.delete(entity);
    }

    @Override
    public Page<InvoiceStatus> findAll(Pageable pageable) {
        return invoiceStatusRespository.findAll(pageable);
    }

    @Override
    public <S extends InvoiceStatus> long count(Example<S> example) {
        return invoiceStatusRespository.count(example);
    }
    @Override
    public List<InvoiceStatus> getALLInvoiceStatusByBill(Integer idBill) {
        return this.invoiceStatusRespository.getALLInvoiceStatusByBill(idBill);
    }
    @Override
    public List<Object[]> getHistoryByBill(Integer id) {
        return this.invoiceStatusRespository.getHistoryByBill(id);
    }
}
