package com.example.shopgiayonepoly.entites;
import com.example.shopgiayonepoly.entites.baseEntity.Base;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction_VNPay")
public class TransactionVNPay extends Base {
    @Column(name = "vnp_TmnCode")
    private String vnpTmnCode;

    @Column(name = "vnp_Amount")
    private String vnpAmount;

    @Column(name = "vnp_BankCode")
    private String vnpBankCode;

    @Column(name = "vnp_BankTranNo")
    private String vnpBankTranNo;

    @Column(name = "vnp_CardType")
    private String vnpCardType;

    @Column(name = "vnp_PayDate")
    private String vnpPayDate;

    @Column(name = "vnp_OrderInfo")
    private String vnpOrderInfo;

    @Column(name = "vnp_TransactionNo")
    private String vnpTransactionNo;

    @Column(name = "vnp_ResponseCode")
    private String vnpResponseCode;

    @Column(name = "vnp_TransactionStatus")
    private String vnpTransactionStatus;

    @Column(name = "vnp_TxnRef")
    private String vnpTxnRef;

    @Column(name = "vnp_SecureHashType")
    private String vnpSecureHashType;

    @Column(name = "vnp_SecureHash")
    private String vnpSecureHash;
}
