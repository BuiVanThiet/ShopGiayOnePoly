package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.dto.request.BillDetailAjax;
import com.example.shopgiayonepoly.dto.request.ProductDetailCheckRequest;
import com.example.shopgiayonepoly.dto.request.SearchClientRequest;
import com.example.shopgiayonepoly.entites.*;
import com.example.shopgiayonepoly.service.BillDetailService;
import com.example.shopgiayonepoly.service.BillService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    String idProductDetail2 = null;

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
        // Lấy IdBill từ session
        Integer idBill = (Integer) session.getAttribute("IdBill");
        System.out.println("id duoc chon luc o home la " + idBill);
        if (idBill == null) {

//            throw new IllegalArgumentException("IdBill không thể là null");
        }
        System.out.println("Session IdBill là: " + idBill);

        // Lấy số trang từ session
        Integer numberPage = (Integer) session.getAttribute("numberPage");
        System.out.println("Số trang từ session là: " + numberPage);

        // Kiểm tra và điều chỉnh số trang
        if (numberPage == null || numberPage < 1) {
            numberPage = 1; // Đặt giá trị mặc định cho số trang
        }
        System.out.println("Số trang sau điều chỉnh là: " + numberPage);

        // Tạo Pageable với chỉ số trang bắt đầu từ 0
        Pageable pageable = PageRequest.of(numberPage - 1, 2);

        // Lấy danh sách BillDetail
        List<BillDetail> billDetailList = this.billDetailService.getBillDetailByIdBill(idBill, pageable).getContent();

        return billDetailList;
    }

    //cai nay dung de lam nut tang gia, so luong
    @PostMapping("/updateBillDetail")
    public ResponseEntity<Map<String,String>> getUpdateBillDetail(@RequestBody BillDetailAjax billDetailAjax) {
        Map<String,String> thongBao = new HashMap<>();
        BillDetail billDetail = this.billDetailService.findById(billDetailAjax.getId()).orElse(new BillDetail());
        System.out.println("id billdetail la: " + billDetail.getId());
        System.out.println("so luong moi la: " + billDetailAjax.getQuantity());
        ProductDetail productDetail   = this.billDetailService.getProductDetailById(billDetail.getProductDetail().getId());
        if(billDetailAjax.getQuantity() <= 0) {
            System.out.println("Sản phẩm không được giảm nhỏ hơn 0!");
            thongBao.put("message","Sản phẩm không được giảm nhỏ hơn 0!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        if(billDetailAjax.getQuantity() < billDetail.getQuantity()) {
            thongBao.put("message","Sửa số lượng sản phẩm thành công!");
            thongBao.put("check","1");
            billDetail.setQuantity(billDetailAjax.getQuantity());
            billDetail.setTotalAmount(billDetail.getPrice().multiply(BigDecimal.valueOf(billDetail.getQuantity())));
            this.billDetailService.save(billDetail);
            return ResponseEntity.ok(thongBao);
        }
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
    public ResponseEntity<Map<String,String>> addProductDetailBuQr(@RequestBody Map<String, String> requestData, HttpSession session) {
        String dataId = requestData.get("id"); // Lấy giá trị từ JSON
        Map<String,String> thongBao = new HashMap<>();
        Bill billById = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(new Bill());

        ProductDetail productDetail = this.billDetailService.getProductDetailById(Integer.parseInt(dataId));

        BillDetail billDetail;

        Integer idBillDetail = this.billDetailService.getBillDetailExist(billById.getId(),productDetail.getId());
        if(idBillDetail != null) {
            billDetail = this.billDetailService.findById(idBillDetail).orElse(new BillDetail());
            billDetail.setQuantity(billDetail.getQuantity()+1);
            billDetail.setTotalAmount(billDetail.getPrice().multiply(BigDecimal.valueOf(billDetail.getQuantity())));
            thongBao.put("message","Sửa số lượng sản phẩm thành công!");
            thongBao.put("check","1");
        }else {
            billDetail = new BillDetail();
            billDetail.setBill(billById);
            billDetail.setProductDetail(productDetail);
            billDetail.setQuantity(1);
            billDetail.setPrice(productDetail.getPrice());
            billDetail.setTotalAmount(billDetail.getPrice().multiply(BigDecimal.valueOf(billDetail.getQuantity())));
            billDetail.setStatus(1);
            thongBao.put("message","Thêm sản phẩm thành công!");
            thongBao.put("check","1");
        }
        this.billDetailService.save(billDetail);
        return ResponseEntity.ok(thongBao);
    }


    @GetMapping("/allProductDetail")
    public List<ProductDetail> getAllProductDetail() {
        return this.billDetailService.getAllProductDetail();
    }

    //

    @GetMapping("/deleteBillDetail/{id}")
    public ModelAndView getDeleteProductDetail(@PathVariable("id") Integer id, HttpSession session) {
        BillDetail billDetail = this.billDetailService.findById(id).orElse(new BillDetail());
        this.billDetailService.delete(billDetail);
        return new ModelAndView("redirect:/bill/bill-detail/"+session.getAttribute("IdBill"));
    }
    //PhanTrang
//    @GetMapping("/page")
//    public Integer numberPage(HttpSession session) {
//        Integer idBill = (Integer) session.getAttribute("IdBill");
//        if (idBill == null) {
//            // Nếu không có IdBill trong session, trả về số trang bằng 0 hoặc lỗi phù hợp
//            return 0;
//        }
//        Integer pageNumber = (int) Math.ceil((double) this.billDetailService.getBillDetailByIdBill(idBill).size() / 2);
//        System.out.println("Số trang là: " + pageNumber);
//        return pageNumber;
//    }
//    @GetMapping("/bill-detail-page/{number}")
//    public void getPageBillDetail(@PathVariable("number") Integer number,HttpSession session) {
//        session.setAttribute("numberPage",number-1);
//        System.out.println("trang dc chon la " + session.getAttribute("numberPage"));
//        System.out.println("id bill " + session.getAttribute("IdBill"));
//    }
    @GetMapping("/productDetail-sell")
    public List<ProductDetail> getProductDetailSell() {
        Pageable pageable = PageRequest.of(0,50);
//        if(productDetailCheckRequest == null) {
        ProductDetailCheckRequest productDetailCheckRequest = new ProductDetailCheckRequest("",null,null,null,null,null,null);
//        }
        return this.billDetailService.getProductDetailSale(productDetailCheckRequest,pageable).getContent();
    }




    //goi khach hang
    @GetMapping("/client")
    public List<Client> getClient() {
        Pageable pageable = PageRequest.of(0,5);
        return this.billService.getClientNotStatus0();
    }
}
