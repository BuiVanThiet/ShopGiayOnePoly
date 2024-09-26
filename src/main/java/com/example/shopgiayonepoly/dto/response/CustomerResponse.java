package com.example.shopgiayonepoly.dto.response;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse extends BaseDTO {
    private String nameImage;
    private String fullName;
    private Integer gender;
    private LocalDate birthDay;
    private String numberPhone;
    private String email;
    private String addRessDetail;

    public CustomerResponse(Integer id, Date createDate, Date updateDate, @NotNull(message = "Mời bạn chọn trạng thái!") Integer status, String nameImage, String fullName, Integer gender, LocalDate birthDay, String numberPhone, String email, String addRessDetail) {
        super(id, createDate, updateDate, status);
        this.nameImage = nameImage;
        this.fullName = fullName;
        this.gender = gender;
        this.birthDay = birthDay;
        this.numberPhone = numberPhone;
        this.email = email;
        this.addRessDetail = addRessDetail;
    }
}
