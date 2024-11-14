package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.request.TransactionCheckRequest;
import com.example.shopgiayonepoly.entites.TransactionVNPay;
import com.example.shopgiayonepoly.repositores.TransactionVNPayRepository;
import com.example.shopgiayonepoly.service.TransactionVNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionVNPayImplement implements TransactionVNPayService {
    @Autowired
    TransactionVNPayRepository transactionVNPayRepository;

    @Override
    public List<TransactionVNPay> findAll() {
        return transactionVNPayRepository.findAll();
    }

    @Override
    public <S extends TransactionVNPay> S save(S entity) {
        return transactionVNPayRepository.save(entity);
    }

    @Override
    public Optional<TransactionVNPay> findById(Integer integer) {
        return transactionVNPayRepository.findById(integer);
    }

    @Override
    public void deleteById(Integer integer) {
        transactionVNPayRepository.deleteById(integer);
    }

    @Override
    public Page<TransactionVNPay> findAll(Pageable pageable) {
        return transactionVNPayRepository.findAll(pageable);
    }
    @Override
    public List<Object[]> getAllTransactionVNPay(
            TransactionCheckRequest transactionCheckRequest
    ) {
        return this.transactionVNPayRepository.getAllTransactionVNPay(
                transactionCheckRequest.getBankCodeList(),
                transactionCheckRequest.getCodeVNPay(),
                transactionCheckRequest.getStarDate(),
                transactionCheckRequest.getEndDate(),
                transactionCheckRequest.getNumberBill(),
                transactionCheckRequest.getTransactionStatus(),
                transactionCheckRequest.getNotePay()
        );
    }

    @Override
    public List<Object[]> getTransactionById(Integer id) {
        return this.transactionVNPayRepository.getTransactionById(id);
    }
}
