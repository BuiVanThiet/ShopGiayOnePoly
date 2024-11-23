package com.example.shopgiayonepoly.dto.request.Shift;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TimekeepingFilterRequest {
    private String searchTerm;
    private Date startDate;
    private Date endDate;
    private String startTime;
    private String endTime;
    private Integer timekeeping_typeCheck;
}
