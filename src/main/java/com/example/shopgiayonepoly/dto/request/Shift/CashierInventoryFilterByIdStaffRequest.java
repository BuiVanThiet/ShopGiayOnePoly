package com.example.shopgiayonepoly.dto.request.Shift;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CashierInventoryFilterByIdStaffRequest {
    private Integer idStaff;
    private String keyStaff;
    private Date startDate;
    private Date endDate;
    private String startTime;
    private String endTime;

    public CashierInventoryFilterByIdStaffRequest(Integer idStaff, Date startDate, Date endDate) {
        this.idStaff = idStaff;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public CashierInventoryFilterByIdStaffRequest(Integer idStaff, Date startDate, Date endDate, String startTime, String endTime) {
        this.idStaff = idStaff;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public CashierInventoryFilterByIdStaffRequest(String keyStaff, Date startDate, Date endDate) {
        this.keyStaff = keyStaff;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
