package com.example.shopgiayonepoly.baseMethod;

import com.example.shopgiayonepoly.dto.request.SaleProductRequest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class BaseSaleProduct {

    public Map<String,String> validateAddAndUpdateSaleProduct(SaleProductRequest saleProductRequest) {
        Map<String,String> thongBao = new HashMap<>();
        //validate danh cho code
        if(saleProductRequest.getCodeSale() == null) {
            thongBao.put("message","Mã giảm giá không được để trống!");
            thongBao.put("check","3");
            return thongBao;
        }

        if(saleProductRequest.getCodeSale().trim().equals("")) {
            thongBao.put("message","Mã giảm giá không được để trống!");
            thongBao.put("check","3");
            return thongBao;
        }

        if(saleProductRequest.getCodeSale().trim().length() > 100) {
            thongBao.put("message","Mã giảm giá không được quá 100 ký tự!");
            thongBao.put("check","3");
            return thongBao;
        }
        //validate ten
        if(saleProductRequest.getNameSale() == null) {
            thongBao.put("message","Tên mã giảm giá không được để trống!");
            thongBao.put("check","3");
            return thongBao;
        }

        if(saleProductRequest.getNameSale().trim().equals("")) {
            thongBao.put("message","Tên mã giảm giá không được để trống!");
            thongBao.put("check","3");
            return thongBao;
        }

        if(saleProductRequest.getNameSale().trim().length() > 255) {
            thongBao.put("message","Tên mã giảm giá không được quá 100 ký tự!");
            thongBao.put("check","3");
            return thongBao;
        }
        //validate danh cho loai giam gia
        if(saleProductRequest.getDiscountType() == null) {
            thongBao.put("message","Không được để trống loại giảm giá!");
            thongBao.put("check","3");
            return thongBao;
        }

        if(saleProductRequest.getDiscountType() == 1 || saleProductRequest.getDiscountType() == 2) {

            if(saleProductRequest.getDiscountValue() == null) {
                thongBao.put("message", "Không được để trống giá trị giảm");
                thongBao.put("check", "3");
                return thongBao;
            }

            if (saleProductRequest.getDiscountType() == 2) {
                if (saleProductRequest.getDiscountValue().compareTo(BigDecimal.ZERO) < 0) {
                    thongBao.put("message", "Loại giảm giá không được nhỏ hơn 0!");
                    thongBao.put("check", "3");
                    return thongBao;
                } else if (saleProductRequest.getDiscountValue().compareTo(new BigDecimal("10000000")) > 0) {
                    thongBao.put("message", "Loại giảm giá không được lớn hơn 10 triệu!");
                    thongBao.put("check", "3");
                    return thongBao;
                }
            }else if (saleProductRequest.getDiscountType() == 1) {
                if (saleProductRequest.getDiscountValue().compareTo(BigDecimal.ZERO) < 0) {
                    thongBao.put("message", "Loại giảm giá không được nhỏ hơn 0%!");
                    thongBao.put("check", "3");
                    return thongBao;
                } else if (saleProductRequest.getDiscountValue().compareTo(new BigDecimal("90")) > 0) {
                    thongBao.put("message", "Loại giảm giá không được lớn hơn 90%!");
                    thongBao.put("check", "3");
                    return thongBao;
                }
            }
        }else {
            thongBao.put("message","Loại giảm giá không hợp lệ!");
            thongBao.put("check","3");
            return thongBao;
        }

        // validate ngay bat dau va ket thuc
        if (saleProductRequest.getStartDate() == null) {
            thongBao.put("message", "Ngày bắt đầu không được để trống!");
            thongBao.put("check", "3");
            return thongBao;
        }

        if (saleProductRequest.getEndDate() == null) {
            thongBao.put("message", "Ngày kết thúc không được để trống!");
            thongBao.put("check", "3");
            return thongBao;
        }

        // Lấy ngày hôm nay
        LocalDate today = LocalDate.now();

        // Kiểm tra ngày bắt đầu phải từ hôm nay trở đi
        if (saleProductRequest.getStartDate().isBefore(today)) {
            thongBao.put("message", "Ngày bắt đầu phải từ ngày hôm nay trở đi!");
            thongBao.put("check", "3");
            return thongBao;
        }

        // Kiểm tra ngày bắt đầu phải sau hoặc bằng ngày kết thúc
        if (!saleProductRequest.getStartDate().isBefore(saleProductRequest.getEndDate())) {
            thongBao.put("message", "Ngày bắt đầu phải sau hoặc bằng ngày kết thúc!");
            thongBao.put("check", "3");
            return thongBao;
        }
        //validate trạng thái
        if(saleProductRequest.getStatus() == null) {
            thongBao.put("message", "Không được để trống trạng thái!");
            thongBao.put("check", "3");
            return thongBao;
        }
        if(saleProductRequest.getStatus() == 1 || saleProductRequest.getStatus() == 2) {

        }else {
            thongBao.put("message", "Trạng thái không đúng định dạng!");
            thongBao.put("check", "3");
            return thongBao;
        }
        thongBao.put("check", "1");
        return thongBao;
    }
}
