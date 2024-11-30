package com.example.shopgiayonepoly.dto.response.client;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilterResponse {
    private List<Integer> categories;
    private List<Integer> manufacturers;
    private List<Integer> materials;
    private List<Integer> origins;
    private Integer minPrice;
    private Integer maxPrice;
    private String priceSort;
}
