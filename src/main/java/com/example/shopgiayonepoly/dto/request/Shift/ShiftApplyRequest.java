package com.example.shopgiayonepoly.dto.request.Shift;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShiftApplyRequest extends BaseDTO {
    private List<Integer> staffIds;
    private Integer idShift;
}
