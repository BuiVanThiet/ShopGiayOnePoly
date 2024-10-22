package com.example.shopgiayonepoly.dto.request;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StaffProfile extends BaseDTO {
    private MultipartFile nameImage;

    private String imageStaffString;

    private String account;

    private String fullName;

    private String email;

    private String numberPhone;

    private Integer gender;

    private LocalDate birthDay;

    private String province;

    private String district;

    private String ward;

    private String addRessDetail;
}
