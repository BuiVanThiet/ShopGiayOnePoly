package com.example.shopgiayonepoly.dto.request.Shift;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import lombok.*;

import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShiftRequest extends BaseDTO{
    private LocalTime startTime;
    private LocalTime endTime;

    public ShiftRequest(Integer id, Date createDate, Date updateDate,Integer status, LocalTime startTime, LocalTime endTime) {
        super(id, createDate, updateDate, status);
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
