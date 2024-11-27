package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.baseMethod.BaseVoucherProduct;
import com.example.shopgiayonepoly.dto.request.VoucherRequest;
import com.example.shopgiayonepoly.dto.request.Voucher_SaleProductSearchRequest;
import com.example.shopgiayonepoly.entites.SaleProduct;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.entites.Voucher;
import com.example.shopgiayonepoly.service.VoucherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api_voucher")
public class VoucherRescontroller extends BaseVoucherProduct{
    @Autowired
    VoucherService voucherService;
    Voucher_SaleProductSearchRequest voucherSearchRequest = null;

    //tai danh sach
    @GetMapping("/list/{page}")
    public List<Object[]> getListVoucher(@PathVariable("page") String page, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        if(voucherSearchRequest == null) {
            voucherSearchRequest = new Voucher_SaleProductSearchRequest(null,"",1);
        }
        System.out.println("du lieu loc: " + voucherSearchRequest.toString());
        List<Object[]> getList = this.voucherService.getVoucherFilter(voucherSearchRequest.getDiscountTypeCheck(),voucherSearchRequest.getNameCheck(),voucherSearchRequest.getStatusCheck());
        System.out.println("benvoucher resst");
        for (Object[] Object: getList) {
            System.out.println(Object[1]);
        }
        System.out.println("het");
        Pageable pageable = PageRequest.of(Integer.parseInt(page)-1,5);
        return convertListToPage(getList,pageable).getContent();
    }

    //phan trang
    @GetMapping("/max-page-voucher")
    public Integer getMaxPageVoucher(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        if(voucherSearchRequest == null) {
            voucherSearchRequest = new Voucher_SaleProductSearchRequest(null,"",1);
        }
        List<Object[]> getList = this.voucherService.getVoucherFilter(voucherSearchRequest.getDiscountTypeCheck(),voucherSearchRequest.getNameCheck(),voucherSearchRequest.getStatusCheck());
        Integer pageNumber = (int) Math.ceil((double) getList.size() / 5);
        return pageNumber;
    }

    //bo loc
    @PostMapping("/search-voucher")
    public Voucher_SaleProductSearchRequest getSearchVoucher(@RequestBody Voucher_SaleProductSearchRequest voucherSearchRequest2,HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        voucherSearchRequest = new Voucher_SaleProductSearchRequest(voucherSearchRequest2.getDiscountTypeCheck(),voucherSearchRequest2.getNameCheck(),voucherSearchRequest2.getStatusCheck());
        if(voucherSearchRequest == null) {
            voucherSearchRequest = new Voucher_SaleProductSearchRequest(null,"",1);
        }
        return voucherSearchRequest;
    }

    //chuc nang them
    @PostMapping("/add-new-voucher")
    public ResponseEntity<Map<String,String>> getMethodAddNewVoucher(@RequestBody VoucherRequest voucherRequest,HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        if(staffLogin.getStatus() != 1) {
            thongBao.put("message","Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        Map<String,String> checkLoginAndLogout = checkLoginAndLogOutByStaff(staffLogin.getId());
        String messMap = checkLoginAndLogout.get("message");
        if(!messMap.trim().equals("")) {
            thongBao.put("message",messMap);
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        Map<String,String> thongBaoValidate = this.validateAddAndUpdateVoucher(voucherRequest);
        if(thongBaoValidate.get("check").equals("1")) {
            System.out.println(voucherRequest.toString());
            List<Voucher> vouchers = this.voucherService.getAll();

            for (Voucher voucher: vouchers) {
                if(voucher.getCodeVoucher().equals(voucherRequest.getCodeVoucher())) {
                    thongBao.put("message","Mã phiếu giảm giá đã tồn tại");
                    thongBao.put("check","3");
                    return ResponseEntity.ok(thongBao);
                }
            }

            voucherService.createNewVoucher(voucherRequest);
            thongBao.put("message","Thêm mới phiếu giảm giá thành công!");
            thongBao.put("check","1");
            return ResponseEntity.ok(thongBao);
        }else {
            return ResponseEntity.ok(thongBaoValidate);
        }
    }

    @GetMapping("/reset-filter-voucher")
    public String getResetFilterVoucher(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }

        voucherSearchRequest = new Voucher_SaleProductSearchRequest(null,"",1);
        return "done";
    }
    // validate checkTrung

    @GetMapping("/check-same-code-voucher")
    public List<String> getAllCodeVouchers(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        List<String> codeVouchers = new ArrayList<>();
        for (Voucher voucher: this.voucherService.getAll()) {
            codeVouchers.add(voucher.getCodeVoucher());
        }
        return codeVouchers;
    }
    ///////////////////////////////////////////////////
    protected Page<Object[]> convertListToPage(List<Object[]> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<Object[]> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }

}
