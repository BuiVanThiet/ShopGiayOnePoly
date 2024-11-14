package com.example.shopgiayonepoly.dto.request;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionCheckRequest {
    private String[] bankCodeList;
    private String codeVNPay;
    private String  starDate;
    private String  endDate;
    private String numberBill;
    private String transactionStatus;
    private String notePay;
}
