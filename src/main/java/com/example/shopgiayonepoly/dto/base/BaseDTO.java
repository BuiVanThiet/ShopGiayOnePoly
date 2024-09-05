package com.example.shopgiayonepoly.dto.base;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseDTO {
    private Integer id;
    private LocalDate createDate;
    private LocalDate updateDate;
    private Integer status;
}
