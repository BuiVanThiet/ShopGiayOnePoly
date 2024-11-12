package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.InvoiceStatus;
import com.example.shopgiayonepoly.repositores.InvoiceStatusRepository;
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
    InvoiceStatusRepository invoiceStatusRepository;

    @Override
    public List<InvoiceStatus> findAll() {
        return invoiceStatusRepository.findAll();
    }

    @Override
    public <S extends InvoiceStatus> S save(S entity) {
        return invoiceStatusRepository.save(entity);
    }

    @Override
    public Optional<InvoiceStatus> findById(Integer integer) {
        return invoiceStatusRepository.findById(integer);
    }

    @Override
    public void deleteById(Integer integer) {
        invoiceStatusRepository.deleteById(integer);
    }

    @Override
    public void delete(InvoiceStatus entity) {
        invoiceStatusRepository.delete(entity);
    }

    @Override
    public Page<InvoiceStatus> findAll(Pageable pageable) {
        return invoiceStatusRepository.findAll(pageable);
    }

    @Override
    public <S extends InvoiceStatus> long count(Example<S> example) {
        return invoiceStatusRepository.count(example);
    }
    @Override
    public List<InvoiceStatus> getALLInvoiceStatusByBill(Integer idBill) {
        return this.invoiceStatusRepository.getALLInvoiceStatusByBill(idBill);
    }
    @Override
    public List<Object[]> getHistoryByBill(Integer id) {
        return this.invoiceStatusRepository.getHistoryByBill(id);
    }

    /////danh rien cho ben client
    @Override
    public List<Object[]> getAllProductBuyClient(Integer id) {
        return this.invoiceStatusRepository.getAllProductBuyClient(id);
    }

    @Override
    public List<Object[]> getBillClient(Integer id) {
        return this.invoiceStatusRepository.getBillClient(id);
    }
    @Override
    public List<Object[]> getInformationBillStatusClient(Integer id) {
        return this.invoiceStatusRepository.getInformationBillStatusClient(id);
    }
}
