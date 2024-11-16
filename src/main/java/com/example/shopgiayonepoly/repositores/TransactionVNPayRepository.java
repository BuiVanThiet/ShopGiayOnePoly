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
            vnp_TransactionNo, --0
            vnp_TxnRef, --1
            FORMAT(CAST(vnp_Amount AS DECIMAL(18, 2)), 'N0') + ' VNĐ' AS vnp_Amount, --2
            vnp_BankCode, --3
            vnp_OrderInfo, --4
            CASE
                 WHEN vnp_TransactionStatus = '00' THEN 'Thành công'
                 ELSE 'Không thành công'
            END AS vnp_TransactionStatus, --5
            CONVERT(DATETIME, 
                STUFF(STUFF(STUFF(vnp_PayDate, 9, 0, ' '), 12, 0, ':'), 15, 0, ':')
            ),--6
            id,--7
            vnp_Amount,--8
            vnp_TmnCode, --9
            vnp_BankTranNo --10
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
