package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.TransactionVNPay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionVNPayRepository extends JpaRepository<TransactionVNPay,Integer> {
    @Query(value = """
    SELECT 
        vnp_TransactionNo, 
        vnp_TxnRef, 
        vnp_Amount, 
        vnp_BankCode, 
        vnp_OrderInfo, 
        vnp_TransactionStatus, 
        CONVERT(DATETIME, 
            STUFF(STUFF(STUFF(vnp_PayDate, 9, 0, ' '), 12, 0, ':'), 15, 0, ':')
        ),
        id
    FROM transaction_VNPay
    WHERE 
        vnp_TransactionNo LIKE %:codeVNPay%
        AND ((:bankCodeList) IS NULL OR vnp_BankCode IN (:bankCodeList))
        AND vnp_TxnRef LIKE %:numberBill%
        AND CAST(SUBSTRING(vnp_PayDate, 1, 8) AS DATETIME) BETWEEN CAST(:starDate AS DATETIME) AND CAST(:endDate AS DATETIME)
        AND (:transactionStatus IS NULL OR
                     (vnp_TransactionStatus = 0 AND :transactionStatus = 0) OR
                     (vnp_TransactionStatus != 0 AND :transactionStatus != 0))
        AND vnp_OrderInfo LIKE %:notePay%
""", nativeQuery = true)
    List<Object[]> getAllTransactionVNPay(
            @Param("bankCodeList") String[] bankCodeList,
            @Param("codeVNPay") String codeVNPay,
            @Param("starDate") String  starDate,
            @Param("endDate") String  endDate,
            @Param("numberBill") String numberBill,
            @Param("transactionStatus") String transactionStatus,
            @Param("notePay") String notePay
    );

    @Query(value = """
    select 
        vnp_TxnRef,
        vnp_TransactionNo,
        vnp_BankCode,
        vnp_Amount,
        vnp_TransactionStatus,
        vnp_OrderInfo,
        vnp_TmnCode,
        vnp_CardType
    from transaction_VNPay where id = :idCheck
""",nativeQuery = true)
    List<Object[]> getTransactionById(@Param("idCheck") Integer id);
}
