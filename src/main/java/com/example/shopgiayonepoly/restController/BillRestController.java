package com.example.shopgiayonepoly.restController;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    String keyVoucher = "";

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

    @GetMapping("/bill-detail-by-id-bill/{numberPage}")
    public List<BillDetail> getBillDetail(@PathVariable("numberPage") Integer pageNumber,HttpSession session) {
        Integer idBill = (Integer) session.getAttribute("IdBill");
        Pageable pageable = PageRequest.of(pageNumber - 1, 2);
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
    @GetMapping("/max-page-billdetail")
    public Integer numberPage(HttpSession session) {
        Integer idBill = (Integer) session.getAttribute("IdBill");
        Integer pageNumber = (int) Math.ceil((double) this.billDetailService.getBillDetailByIdBill(idBill).size() / 2);
        System.out.println("Số trang là: " + pageNumber);
        return pageNumber;
    }
//    @GetMapping("/bill-detail-page/{number}")
//    public void getPageBillDetail(@PathVariable("number") Integer number,HttpSession session) {
//        session.setAttribute("numberPage",number-1);
//        System.out.println("trang dc chon la " + session.getAttribute("numberPage"));
//        System.out.println("id bill " + session.getAttribute("IdBill"));
//    }
    @GetMapping("/productDetail-sell/{pageNumber}")
    public List<ProductDetail> getProductDetailSell(@PathVariable("pageNumber") Integer pageNumber, HttpSession session) {
        Pageable pageable = PageRequest.of(pageNumber-1,5);
        if(this.productDetailCheckRequest == null) {
            this.productDetailCheckRequest = new ProductDetailCheckRequest("",null,null,null,null,null,null);
        }
        List<ProductDetail> productDetails = this.billDetailService.getProductDetailSale(this.productDetailCheckRequest);
        System.out.println("Số lượng 1 trang la " + productDetails.size());
        return convertListToPage(productDetails,pageable).getContent();
    }

    @GetMapping("/page-max-product")
    public Integer getMaxPageProduct() {
        if(this.productDetailCheckRequest == null) {
            this.productDetailCheckRequest = new ProductDetailCheckRequest("",null,null,null,null,null,null);
        }
        Integer maxPageProduct = (int) Math.ceil((double) this.billDetailService.getProductDetailSale(this.productDetailCheckRequest).size() / 5);
        System.out.println("so trang cua san pham " + maxPageProduct);
        return maxPageProduct;
    }
    @PostMapping("/filter-product-deatail")
    public ResponseEntity<?> getFilterProduct(@RequestBody ProductDetailCheckRequest productDetailCheckRequest2, HttpSession session) {
        if(productDetailCheckRequest2.getIdCategories().get(0) == 0) {
            productDetailCheckRequest2.setIdCategories(null);
        }
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
        System.out.println(clientBillInformationResponse.toString());
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
        System.out.println("da loc duoc " + this.keyVoucher);
        System.out.println(session.getAttribute("IdBill"));
        return this.billService.getVouCherByBill((Integer) session.getAttribute("IdBill"),keyVoucher,pageable).getContent();
    }

    @PostMapping("/voucher-search")
    public ResponseEntity<?> getSearchVoucher(@RequestBody Map<String, String> voucherSearch) {
        String keyword = voucherSearch.get("keyword");
        this.keyVoucher = keyword;
        System.out.println("du lieu loc vc " + voucherSearch);
        return ResponseEntity.ok("Done v");
    }

    @GetMapping("/max-page-voucher")
    public Integer getMaxPageVoucher(HttpSession session) {
        System.out.println("ID BILL LA" +session.getAttribute("IdBill"));
        Integer maxPage = (int) Math.ceil((double) this.billService.getVoucherByBill((Integer) session.getAttribute("IdBill"), this.keyVoucher).size() / 5);
        System.out.println("so tramg toi da cua voucher " + maxPage);
        return maxPage;
    }

    @GetMapping("/categoryAll")
    public List<Category> getAllCategory() {
        return this.billDetailService.getAllCategores();
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
//    Phân trang cho product
    public Page<ProductDetail> convertListToPage(List<ProductDetail> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<ProductDetail> sublist = list.subList(start, end);

        return new PageImpl<>(sublist, pageable, list.size());
    }

}
