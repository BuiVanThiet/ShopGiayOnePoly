package com.example.shopgiayonepoly.dto.request.bill;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchBillByStatusRequest {
    private List<Integer> statusSearch;
}
