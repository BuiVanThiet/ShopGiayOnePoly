package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.dto.request.VoucherRequest;
import com.example.shopgiayonepoly.dto.request.Voucher_SaleProductSearchRequest;
import com.example.shopgiayonepoly.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api_voucher")
public class VoucherRescontroller {
    @Autowired
    VoucherService voucherService;
    Voucher_SaleProductSearchRequest voucherSearchRequest = null;

    //tai danh sach
    @GetMapping("/list/{page}")
    public List<Object[]> getListVoucher(@PathVariable("page") String page) {
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
    public Integer getMaxPageVoucher() {
        if(voucherSearchRequest == null) {
            voucherSearchRequest = new Voucher_SaleProductSearchRequest(null,"",1);
        }
        List<Object[]> getList = this.voucherService.getVoucherFilter(voucherSearchRequest.getDiscountTypeCheck(),voucherSearchRequest.getNameCheck(),voucherSearchRequest.getStatusCheck());
        Integer pageNumber = (int) Math.ceil((double) getList.size() / 5);
        return pageNumber;
    }

    //bo loc
    @PostMapping("/search-voucher")
    public Voucher_SaleProductSearchRequest getSearchVoucher(@RequestBody Voucher_SaleProductSearchRequest voucherSearchRequest2) {
        voucherSearchRequest = new Voucher_SaleProductSearchRequest(voucherSearchRequest2.getDiscountTypeCheck(),voucherSearchRequest2.getNameCheck(),voucherSearchRequest2.getStatusCheck());
        if(voucherSearchRequest == null) {
            voucherSearchRequest = new Voucher_SaleProductSearchRequest(null,"",1);
        }
        return voucherSearchRequest;
    }

    //chuc nang them
    @PostMapping("/add-new-voucher")
    public ResponseEntity<Map<String,String>> getMethodAddNewVoucher(@RequestBody VoucherRequest voucherRequest) {
        Map<String,String> thongBao = new HashMap<>();

        System.out.println(voucherRequest.toString());

        voucherService.createNewVoucher(voucherRequest);
        thongBao.put("message","Thêm mới phiếu gia giá thành công!");
        thongBao.put("check","1");
        return ResponseEntity.ok(thongBao);
    }

    @GetMapping("/reset-filter-voucher")
    public String getResetFilterVoucher() {
        voucherSearchRequest = new Voucher_SaleProductSearchRequest(null,"",1);
        return "done";
    }

    ///////////////////////////////////////////////////
    protected Page<Object[]> convertListToPage(List<Object[]> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<Object[]> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }

}