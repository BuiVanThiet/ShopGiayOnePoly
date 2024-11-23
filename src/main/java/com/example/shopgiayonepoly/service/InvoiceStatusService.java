package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.InvoiceStatus;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface InvoiceStatusService {
    List<InvoiceStatus> findAll();

    <S extends InvoiceStatus> S save(S entity);

    Optional<InvoiceStatus> findById(Integer integer);

    void deleteById(Integer integer);

    void delete(InvoiceStatus entity);

    Page<InvoiceStatus> findAll(Pageable pageable);

    <S extends InvoiceStatus> long count(Example<S> example);

    List<InvoiceStatus> getALLInvoiceStatusByBill(Integer idBill);

    List<Object[]> getHistoryByBill(Integer id);

    /////danh rien cho ben client
    List<Object[]> getAllProductBuyClient(Integer id);

    List<Object[]> getBillClient(Integer id);

    List<Object[]> getInformationBillStatusClient(Integer id);

    List<Object[]> getAllInvoiceStatusByStaff(
            Integer idStaff,
            Date startDate,
            Date endDate,
            String startTime,
            String endTime
    );
}
