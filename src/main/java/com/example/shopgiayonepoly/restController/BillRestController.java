package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.dto.request.BillDetailAjax;
import com.example.shopgiayonepoly.entites.*;
import com.example.shopgiayonepoly.implement.BillDetailImplement;
import com.example.shopgiayonepoly.implement.BillImplement;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bill-api")
public class BillRestController {
    @Autowired
    BillImplement billImplement;
    @Autowired
    BillDetailImplement billDetailImplement;
    String idProductDetail = null;
    @GetMapping("/get-idbill")
    @ResponseBody
    public Integer getIdBillFromSession(HttpSession session) {
        return (Integer) session.getAttribute("IdBill");
    }

    @GetMapping("/all")
    public List<Bill> getAll() {
        return billImplement.findAll();
    }

    @GetMapping("/all-new")
    public List<Bill> getAllNew() {
        Pageable pageable = PageRequest.of(0,5);
        return billImplement.getBillByStatusNew(pageable);
    }

    @GetMapping("/bill-detail-by-id-bill")
    public List<BillDetail> getBillDetail(HttpSession session) {
        System.out.println("sesion la" + session.getAttribute("IdBill"));
        Pageable pageable = PageRequest.of(0,5);
        return this.billDetailImplement.getBillDetailByIdBill((Integer) session.getAttribute("IdBill"),pageable);
    }
    //cai nay dung de lam nut tang gia, so luong
    @PostMapping("/updateBillDetail")
    public ResponseEntity<?> getUpdateBillDetail(@RequestBody BillDetailAjax billDetailAjax) {
        BillDetail billDetail = this.billDetailImplement.findById(billDetailAjax.getId()).orElse(new BillDetail());
        System.out.println("id billdetail la: " + billDetail.getId());
        System.out.println("so luong moi la: " + billDetailAjax.getQuantity());
        return ResponseEntity.ok("Cap nhat thanh cong!");
    }
    //THEM BSP BANG QR
    @PostMapping("/addProductByQr")
    public ModelAndView addProductDetailBuQr(@RequestBody Map<String, String> requestData, HttpSession session) {
        String dataId = requestData.get("id"); // Lấy giá trị từ JSON

        if(dataId != null) {
            Bill billById = this.billImplement.findById((Integer) session.getAttribute("IdBill")).orElse(new Bill());

            ProductDetail productDetail = this.billDetailImplement.getProductDetailById(Integer.parseInt(dataId));

            BillDetail billDetail = new BillDetail();
            billDetail.setBill(billById);
            billDetail.setProductDetail(productDetail);
            billDetail.setQuantity(1);
            billDetail.setPrice(productDetail.getPrice());
            billDetail.setTotalAmount(billDetail.getPrice().multiply(BigDecimal.valueOf(billDetail.getQuantity())));
            billDetail.setStatus(1);
            this.billDetailImplement.save(billDetail);
            return new ModelAndView("redirect:/bill/home");
        }else {
            return new ModelAndView("error");
        }
    }

    @GetMapping("/allProductDetail")
    public List<ProductDetail> getAllProductDetail() {
        return this.billDetailImplement.getAllProductDetail();
    }

    //

    @GetMapping("/deleteBillDetail/{id}")
    private ModelAndView getDeleteProductDetail(@PathVariable("id") Integer id, HttpSession session) {
        BillDetail billDetail = this.billDetailImplement.findById(id).orElse(new BillDetail());
        this.billDetailImplement.delete(billDetail);
        return new ModelAndView("redirect:/bill/bill-detail/"+session.getAttribute("IdBill"));
    }

}
