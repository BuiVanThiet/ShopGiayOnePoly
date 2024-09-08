package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.dto.request.BillDetailAjax;
import com.example.shopgiayonepoly.entites.*;
import com.example.shopgiayonepoly.service.BillDetailService;
import com.example.shopgiayonepoly.service.BillService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bill-api")
public class BillRestController {
    @Autowired
    BillService billService;
    @Autowired
    BillDetailService billDetailService;
    String idProductDetail = null;
    @GetMapping("/get-idbill")
    @ResponseBody
    public Integer getIdBillFromSession(HttpSession session) {
        return (Integer) session.getAttribute("IdBill");
    }

    @GetMapping("/all")
    public List<Bill> getAll() {
        return billService.findAll();
    }

    @GetMapping("/all-new")
    public List<Bill> getAllNew() {
        Pageable pageable = PageRequest.of(0,5);
        return billService.getBillByStatusNew(pageable);
    }

    @GetMapping("/bill-detail-by-id-bill")
    public List<BillDetail> getBillDetail(HttpSession session) {
        System.out.println("sesion la" + session.getAttribute("IdBill"));
        Pageable pageable = PageRequest.of(0,5);
        return this.billDetailService.getBillDetailByIdBill((Integer) session.getAttribute("IdBill"),pageable);
    }
    //cai nay dung de lam nut tang gia, so luong
    @PostMapping("/updateBillDetail")
    public ResponseEntity<Map<String,String>> getUpdateBillDetail(@RequestBody BillDetailAjax billDetailAjax) {
        Map<String,String> thongBao = new HashMap<>();
        BillDetail billDetail = this.billDetailService.findById(billDetailAjax.getId()).orElse(new BillDetail());
        System.out.println("id billdetail la: " + billDetail.getId());
        System.out.println("so luong moi la: " + billDetailAjax.getQuantity());
        ProductDetail productDetail   = this.billDetailService.getProductDetailById(billDetail.getProductDetail().getId());
        if(productDetail.getStatus() == 0 || productDetail.getStatus() == 2 || productDetail.getProduct().getStatus() == 0 || productDetail.getProduct().getStatus() == 2) {
            System.out.println("San pham nay tren he thong da ngung ban hoax bi xoa!");
            thongBao.put("message","Sản phẩm đã bị xóa hoặc ngừng bán trên hệ thống!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        thongBao.put("message","Sửa số lượng sản phẩm thành công!");
        thongBao.put("check","1");
        billDetail.setQuantity(billDetailAjax.getQuantity());
        billDetail.setTotalAmount(billDetail.getPrice().multiply(BigDecimal.valueOf(billDetail.getQuantity())));
        this.billDetailService.save(billDetail);
        return ResponseEntity.ok(thongBao);
    }
    //THEM BSP BANG QR
    @PostMapping("/addProductByQr")
    public ModelAndView addProductDetailBuQr(@RequestBody Map<String, String> requestData, HttpSession session) {
        String dataId = requestData.get("id"); // Lấy giá trị từ JSON

        if(dataId != null) {
            Bill billById = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(new Bill());

            ProductDetail productDetail = this.billDetailService.getProductDetailById(Integer.parseInt(dataId));

            BillDetail billDetail;

            Integer idBillDetail = this.billDetailService.getBillDetailExist(billById.getId(),productDetail.getId());
            if(idBillDetail != null) {
                billDetail = this.billDetailService.findById(idBillDetail).orElse(new BillDetail());
                billDetail.setQuantity(billDetail.getQuantity()+1);
                billDetail.setTotalAmount(billDetail.getPrice().multiply(BigDecimal.valueOf(billDetail.getQuantity())));
            }else {
                billDetail = new BillDetail();
                billDetail.setBill(billById);
                billDetail.setProductDetail(productDetail);
                billDetail.setQuantity(1);
                billDetail.setPrice(productDetail.getPrice());
                billDetail.setTotalAmount(billDetail.getPrice().multiply(BigDecimal.valueOf(billDetail.getQuantity())));
                billDetail.setStatus(1);
            }
            this.billDetailService.save(billDetail);
            return new ModelAndView("redirect:/bill/home");
        }else {
            return new ModelAndView("error");
        }
    }


    @GetMapping("/allProductDetail")
    public List<ProductDetail> getAllProductDetail() {
        return this.billDetailService.getAllProductDetail();
    }

    //

    @GetMapping("/deleteBillDetail/{id}")
    private ModelAndView getDeleteProductDetail(@PathVariable("id") Integer id, HttpSession session) {
        BillDetail billDetail = this.billDetailService.findById(id).orElse(new BillDetail());
        this.billDetailService.delete(billDetail);
        return new ModelAndView("redirect:/bill/bill-detail/"+session.getAttribute("IdBill"));
    }

}
