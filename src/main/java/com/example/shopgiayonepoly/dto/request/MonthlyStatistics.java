package com.example.shopgiayonepoly.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyStatistics {
    private String month; // Tháng (MM-yyyy)
    private int invoiceCount; // Số lượng hóa đơn
    private int productCount;
}
