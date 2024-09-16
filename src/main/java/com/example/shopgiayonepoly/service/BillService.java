package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.response.BillTotalInfornationResponse;
import com.example.shopgiayonepoly.dto.response.ClientBillInformationResponse;
import com.example.shopgiayonepoly.entites.Bill;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface BillService {
    List<Bill> findAll();

    <S extends Bill> S save(S entity);

    Optional<Bill> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    List<Bill> findAll(Sort sort);

    Page<Bill> findAll(Pageable pageable);

    List<Bill> getBillByStatusNew(Pageable pageable);

    List<Customer> getClientNotStatus0();

    BillTotalInfornationResponse findBillVoucherById(Integer id);
    List<ClientBillInformationResponse> getClientBillInformationResponse(Integer idClient);

    List<Voucher> getVouCherByBill(Integer idBill);
}
