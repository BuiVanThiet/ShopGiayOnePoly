package com.example.shopgiayonepoly.restController;

import com.beust.ah.A;
import com.example.shopgiayonepoly.dto.request.BillDetailAjax;
import com.example.shopgiayonepoly.dto.request.PayMethodRequest;
import com.example.shopgiayonepoly.dto.request.ProductDetailCheckRequest;
import com.example.shopgiayonepoly.dto.response.BillTotalInfornationResponse;
import com.example.shopgiayonepoly.dto.response.ClientBillInformationResponse;
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
import java.util.*;

@RestController
@RequestMapping("/bill-api")
public class BillRestController {
    ProductDetailCheckRequest productDetailCheckRequest;
    @Autowired
    BillService billService;
    @Autowired
    BillDetailService billDetailService;

    Integer pageProduct = 0;

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
        Pageable pageable = PageRequest.of(0,10);
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
        if(billDetailAjax.getQuantity() > billDetail.getProductDetail().getQuantity()) {
            System.out.println("Số lượng mua không được quá số lượng trong hệ thống!");
            thongBao.put("message","Số lượng mua không được quá số lượng trong hệ thống!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        if(billDetailAjax.getQuantity() > 10) {
            System.out.println("Hiện tại cửa hàng chỉ bán mỗi sản phẩm số lượng không quá 10!");
            thongBao.put("message","Hiện tại cửa hàng chỉ bán mỗi sản phẩm số lượng không quá 10!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
//        if(billDetailAjax.getQuantity() < billDetail.getQuantity()) {
//            thongBao.put("message","Sửa số lượng sản phẩm thành công!");
//            thongBao.put("check","1");
//            billDetail.setQuantity(billDetailAjax.getQuantity());
//            billDetail.setTotalAmount(billDetail.getPrice().multiply(BigDecimal.valueOf(billDetail.getQuantity())));
//            this.billDetailService.save(billDetail);
//            return ResponseEntity.ok(thongBao);
//        }
        if(productDetail.getStatus() == 0 || productDetail.getStatus() == 2 || productDetail.getProduct().getStatus() == 0 || productDetail.getProduct().getStatus() == 2) {
            thongBao.put("message","Sản phẩm đã bị xóa hoặc ngừng bán trên hệ thống!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        thongBao.put("message","Sửa số lượng sản phẩm thành công!");
        thongBao.put("check","1");
        billDetail.setQuantity(billDetailAjax.getQuantity());
        billDetail.setTotalAmount(billDetail.getPrice().multiply(BigDecimal.valueOf(billDetail.getQuantity())));
        this.billDetailService.save(billDetail);

        this.setTotalAmount(billDetail.getBill());

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
        System.out.println("Thong tin bill: " + billDetail.getBill().toString());

        this.setTotalAmount(billById);

        return ResponseEntity.ok(thongBao);
    }


    @GetMapping("/allProductDetail")
    public List<ProductDetail> getAllProductDetail() {
        return this.billDetailService.getAllProductDetail();
    }

    //
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
    @GetMapping("/productDetail-sell/{pageNumber}")
    public List<ProductDetail> getProductDetailSell(@PathVariable("pageNumber") Integer pageNumber, HttpSession session) {
        this.pageProduct = pageNumber - 1;
        Pageable pageable = PageRequest.of(pageProduct,5);
        if(this.productDetailCheckRequest == null) {
//            ArrayList<Integer> categoryList = new ArrayList<>();
//            categoryList.add(1);
//            categoryList.add(4);
            this.productDetailCheckRequest = new ProductDetailCheckRequest("",null,null,null,null,null);
        }
        List<ProductDetail> productDetails = this.billDetailService.getProductDetailSale(this.productDetailCheckRequest,pageable).getContent();
        Integer maxPageProduct = (int) Math.ceil((double) this.billDetailService.countProductDetailSale(this.productDetailCheckRequest) / 5);
        return productDetails;
    }

    @GetMapping("/page-max-product")
    public Integer getMaxPageProduct() {
        if(this.productDetailCheckRequest == null) {
//            ArrayList<Integer> categoryList = new ArrayList<>();
//            categoryList.add(1);
//            categoryList.add(4);
            this.productDetailCheckRequest = new ProductDetailCheckRequest("",null,null,null,null,null);
        }
        Integer maxPageProduct = (int) Math.ceil((double) this.billDetailService.countProductDetailSale(this.productDetailCheckRequest) / 5);
        System.out.println("so trang cua san pham " + maxPageProduct);
        return maxPageProduct;
    }
    @PostMapping("/filter-product-deatail")
    public ResponseEntity<?> getFilterProduct(@RequestBody ProductDetailCheckRequest productDetailCheckRequest2, HttpSession session) {
        this.productDetailCheckRequest = productDetailCheckRequest2;
        System.out.println("Thong tin loc " + this.productDetailCheckRequest.toString());
        return ResponseEntity.ok("Done");
    }

    //goi khach hang
    @GetMapping("/client")
    public List<Customer> getClient() {
        Pageable pageable = PageRequest.of(0,5);
        return this.billService.getClientNotStatus0();
    }

    @GetMapping("/payment-information")
    public BillTotalInfornationResponse billTotalInfornationResponse(HttpSession session) {
        return this.billService.findBillVoucherById((Integer) session.getAttribute("IdBill"));
    }

    @GetMapping("/client-bill-information")
    public ClientBillInformationResponse getClientBillInformation(HttpSession session) {
        List<ClientBillInformationResponse> clientBillInformationResponses = this.billService.getClientBillInformationResponse((Integer) session.getAttribute("IdClient"));
        ClientBillInformationResponse clientBillInformationResponse = clientBillInformationResponses.get(0);
        String getAddRessDetail = clientBillInformationResponse.getAddressDetail();
        String[] part = getAddRessDetail.split(",\\s*");
        clientBillInformationResponse.setCommune(part[0]);
        clientBillInformationResponse.setDistrict(part[1]);
        clientBillInformationResponse.setCity(part[2]);
        clientBillInformationResponse.setAddressDetail(String.join(", ", java.util.Arrays.copyOfRange(part, 3, part.length)));
        return clientBillInformationResponse;
    }

    @PostMapping("/uploadPaymentMethod")
    public Bill getUploadBillPay(@RequestBody PayMethodRequest payMethodRequest,HttpSession session) {
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(new Bill());
        bill.setPaymentMethod(payMethodRequest.getPayMethod());
        bill.setUpdateDate(new Date());
        return this.billService.save(bill);
    }

    @GetMapping("/voucher/{page}")
    public List<Voucher> getVouCherList(@PathVariable("page") Integer pageNumber,HttpSession session) {
        Pageable pageable = PageRequest.of(pageNumber-1,5);
        return this.billService.getVouCherByBill((Integer) session.getAttribute("IdBill"));
    }



    public void setTotalAmount(Bill bill) {
        BigDecimal total = this.billDetailService.getTotalAmountByIdBill(bill.getId());
        if(total != null) {
            bill.setUpdateDate(new Date());
            bill.setTotalAmount(total);
        }else {
            bill.setUpdateDate(new Date());
            bill.setTotalAmount(BigDecimal.valueOf(0));
        }
        System.out.println("Thong tin bill: " + bill.toString());

        this.billService.save(bill);
    }

}
