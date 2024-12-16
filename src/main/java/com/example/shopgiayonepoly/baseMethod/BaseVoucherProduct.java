package com.example.shopgiayonepoly.baseMethod;

import com.example.shopgiayonepoly.dto.request.SaleProductRequest;
import com.example.shopgiayonepoly.dto.request.VoucherRequest;
import com.example.shopgiayonepoly.service.TimekeepingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseVoucherProduct {
    @Autowired
    protected TimekeepingService timekeepingService;
    public Map<String,String> validateAddAndUpdateVoucher(VoucherRequest voucherRequest) {
        Map<String,String> thongBao = new HashMap<>();
        //validate danh cho code
        if(voucherRequest.getCodeVoucher() == null) {
            thongBao.put("message","Mã phiếu giảm giá không được để trống!");
            thongBao.put("check","3");
            return thongBao;
        }

        if(voucherRequest.getCodeVoucher().trim().equals("")) {
            thongBao.put("message","Mã giảm giá không được để trống!");
            thongBao.put("check","3");
            return thongBao;
        }

        if(voucherRequest.getCodeVoucher().trim().length() > 100) {
            thongBao.put("message","Mã phiếu giảm giá không được quá 100 ký tự!");
            thongBao.put("check","3");
            return thongBao;
        }
        //validate ten
        if(voucherRequest.getNameVoucher() == null) {
            thongBao.put("message","Tên phiếu giảm giá không được để trống!");
            thongBao.put("check","3");
            return thongBao;
        }

        if(voucherRequest.getNameVoucher().trim().equals("")) {
            thongBao.put("message","Tên phiếu giảm giá không được để trống!");
            thongBao.put("check","3");
            return thongBao;
        }

        if(voucherRequest.getNameVoucher().trim().length() > 255) {
            thongBao.put("message","Tên phiếu giảm giá không được quá 100 ký tự!");
            thongBao.put("check","3");
            return thongBao;
        }
        //validate danh cho loai giam gia
        if(voucherRequest.getDiscountType() == null) {
            thongBao.put("message","Không được để trống loại giảm giá!");
            thongBao.put("check","3");
            return thongBao;
        }

        if(voucherRequest.getDiscountType() == 1 || voucherRequest.getDiscountType() == 2) {
            //gia tri giam
            if(voucherRequest.getPriceReduced() == null) {
                thongBao.put("message", "Không được để trống giá trị giảm");
                thongBao.put("check", "3");
                return thongBao;
            }
//            gia tri ap dung
            if(voucherRequest.getPricesApply() == null) {
                thongBao.put("message", "Không được để trống giá trị áp dụng");
                thongBao.put("check", "3");
                return thongBao;
            }
            //gia tri toi da
            if(voucherRequest.getPricesMax() == null) {
                thongBao.put("message", "Không được để trống giá trị giảm tối đa");
                thongBao.put("check", "3");
                return thongBao;
            }

            if(isIntegerValue(voucherRequest.getPriceReduced()) == false) {
                thongBao.put("message", "Giá trị giảm phải là số thực!");
                thongBao.put("check", "3");
                return thongBao;
            }

            if(isIntegerValue(voucherRequest.getPricesApply()) == false) {
                thongBao.put("message", "Giá trị áp dụng phải là số thực!");
                thongBao.put("check", "3");
                return thongBao;
            }

            if(isIntegerValue(voucherRequest.getPricesMax()) == false) {
                thongBao.put("message", "Giá trị giảm tối đa phải là số thực!");
                thongBao.put("check", "3");
                return thongBao;
            }

            if (voucherRequest.getDiscountType() == 2) {
                if (voucherRequest.getPriceReduced().compareTo(new BigDecimal(10000)) < 0) {
                    thongBao.put("message", "Loại giảm giá không được nhỏ hơn 10 nghìn!");
                    thongBao.put("check", "3");
                    return thongBao;
                } else if (voucherRequest.getPriceReduced().compareTo(new BigDecimal("10000000")) > 0) {
                    thongBao.put("message", "Loại giảm giá không được lớn hơn 10 triệu!");
                    thongBao.put("check", "3");
                    return thongBao;
                }

            }else if (voucherRequest.getDiscountType() == 1) {
                if (voucherRequest.getPriceReduced().compareTo(BigDecimal.ZERO) < 0) {
                    thongBao.put("message", "Loại giảm giá không được nhỏ hơn 0%!");
                    thongBao.put("check", "3");
                    return thongBao;
                } else if (voucherRequest.getPriceReduced().compareTo(new BigDecimal("90")) > 0) {
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
        // Kiểm tra nếu giá trị giảm không lớn hơn giá trị giảm tối đa
        if (voucherRequest.getPriceReduced().compareTo(voucherRequest.getPricesMax()) > 0) {
            thongBao.put("message", "Giá trị giảm không được lớn hơn giá trị giảm tối đa!");
            thongBao.put("check", "3");
            return thongBao;
        }

        // Kiểm tra nếu giá trị giảm không lớn hơn giá trị áp dụng
        if (voucherRequest.getPriceReduced().compareTo(voucherRequest.getPricesApply()) > 0) {
            thongBao.put("message", "Giá trị giảm không được lớn hơn giá trị áp dụng!");
            thongBao.put("check", "3");
            return thongBao;
        }

        // Kiểm tra giá trị áp dụng phải lớn hơn hoặc bằng giá trị giảm tối đa
        if (voucherRequest.getPricesApply().compareTo(voucherRequest.getPricesMax()) < 0) {
            thongBao.put("message", "Giá trị áp dụng phải lớn hơn hoặc bằng giá trị giảm tối đa!");
            thongBao.put("check", "3");
            return thongBao;
        }

        // validate ngay bat dau va ket thuc
        if (voucherRequest.getStartDate() == null) {
            thongBao.put("message", "Ngày bắt đầu không được để trống!");
            thongBao.put("check", "3");
            return thongBao;
        }

        if (voucherRequest.getEndDate() == null) {
            thongBao.put("message", "Ngày kết thúc không được để trống!");
            thongBao.put("check", "3");
            return thongBao;
        }

        // Lấy ngày hôm nay
        LocalDate today = LocalDate.now();

        // Kiểm tra ngày bắt đầu phải từ hôm nay trở đi
        if (voucherRequest.getStartDate().isBefore(today)) {
            thongBao.put("message", "Ngày bắt đầu phải từ ngày hôm nay trở đi!");
            thongBao.put("check", "3");
            return thongBao;
        }

        // Kiểm tra ngày bắt đầu phải sau hoặc bằng ngày kết thúc
        if (!voucherRequest.getStartDate().isBefore(voucherRequest.getEndDate())) {
            thongBao.put("message", "Ngày bắt đầu phải sau hoặc bằng ngày kết thúc!");
            thongBao.put("check", "3");
            return thongBao;
        }
        //validate trạng thái
        if(voucherRequest.getStatus() == null) {
            thongBao.put("message", "Không được để trống trạng thái!");
            thongBao.put("check", "3");
            return thongBao;
        }
        if(voucherRequest.getStatus() == 1 || voucherRequest.getStatus() == 2) {

        }else {
            thongBao.put("message", "Trạng thái không đúng định dạng!");
            thongBao.put("check", "3");
            return thongBao;
        }
        thongBao.put("check", "1");
        return thongBao;
    }

    public Boolean isIntegerValue(BigDecimal value) {
        if (value == null) {
            return false; // hoặc true tùy theo logic bạn muốn
        }
        // Kiểm tra nếu phần dư khi chia 1 bằng 0
        return value.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0;
    }

    protected Map<String,String> checkLoginAndLogOutByStaff(Integer idStaff) {
        Map<String,String> thongBao = new HashMap<>();
        String checkLogin = getCheckStaffAttendanceYetBill(idStaff,1);
        String checkLogOut = getCheckStaffAttendanceYetBill(idStaff,2);
        if(!checkLogin.equals("Có")) {
            thongBao.put("message","Mời bạn điểm danh trước khi làm việc!");
            return thongBao;
        }

        if(checkLogin.equals("Có") && checkLogOut.equals("Có")) {
            thongBao.put("message","Bạn đã điểm danh vào và ra rồi, không thể làm việc được nữa!");
            return thongBao;
        }
        thongBao.put("message","");
        return thongBao;
    }

    protected String getCheckStaffAttendanceYetBill(
//            @PathVariable("id") Integer idStaff,@PathVariable("type") Integer timekeepingTypeCheck
            Integer idStaff, Integer timekeepingTypeCheck
    ) {
        List<Object[]> checkLoginLogOut = this.timekeepingService.getCheckStaffAttendanceYet(idStaff, timekeepingTypeCheck);

        // Kiểm tra nếu danh sách không rỗng và có kết quả
        if (!checkLoginLogOut.isEmpty() && checkLoginLogOut.get(0).length > 0) {
            // Lấy giá trị đầu tiên từ kết quả
            return checkLoginLogOut.get(0)[0].toString();
        }
        // Trường hợp không có dữ liệu
        return "Không";
    }

}
