package com.example.shopgiayonepoly.dto.request.Shift;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftFilterRequest {
    private String startTimeBegin;
    private String startTimeEnd;
    private String endTimeBegin;
    private String endTimeEnd;
    private Integer statusShift;
    private Integer status;
}
