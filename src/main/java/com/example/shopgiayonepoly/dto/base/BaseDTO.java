package com.example.shopgiayonepoly.dto.base;

import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseDTO {
    private Integer id;
    private Date createDate;
    private Date updateDate;
    private Integer status;
}
