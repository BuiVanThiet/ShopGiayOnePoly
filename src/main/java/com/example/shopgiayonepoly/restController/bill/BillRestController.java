package com.example.shopgiayonepoly.restController.bill;

import com.example.shopgiayonepoly.baseMethod.BaseBill;
import com.example.shopgiayonepoly.dto.request.bill.*;
import com.example.shopgiayonepoly.dto.response.bill.*;
import com.example.shopgiayonepoly.entites.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/bill-api")
public class BillRestController extends BaseBill {
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
        keyVoucher = "";
        productDetailCheckRequest = new ProductDetailCheckRequest("",null,null,null,null,null,null);
        Pageable pageable = PageRequest.of(0,10);
        return billService.getBillByStatusNew(pageable);
    }

    @GetMapping("/bill-detail-by-id-bill/{numberPage}")
    public List<BillDetail> getBillDetail(@PathVariable("numberPage") Integer pageNumber,HttpSession session) {
        Integer idBill = (Integer) session.getAttribute("IdBill");
        Pageable pageable = PageRequest.of(pageNumber - 1, 2);
        keyVoucher = "";
        productDetailCheckRequest = new ProductDetailCheckRequest("",null,null,null,null,null,null);
        // Lấy danh sách BillDetail
        List<BillDetail> billDetailList = this.billDetailService.getBillDetailByIdBill(idBill, pageable).getContent();

        getDeleteVoucherByBill(idBill);

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
        if(productDetail.getQuantity() ==  0 && billDetailAjax.getMethod().equals("cong")) {
            System.out.println("Số lượng mua không được quá số lượng trong hệ thống!");
            thongBao.put("message","Hiện tại sản phẩm bạn mua đã hết!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        if(billDetailAjax.getQuantity() > 10 && billDetailAjax.getMethod().equals("cong")) {
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
        Integer quantityUpdate = 0;
        if(billDetailAjax.getMethod().equals("cong")) {
            quantityUpdate = 1;
        }else {
            quantityUpdate =-1;
        }
        Integer statusUpdate = billDetail.getQuantity() > billDetailAjax.getQuantity() ?  2 : 1;

        billDetail.setQuantity(billDetail.getQuantity()+quantityUpdate);

        billDetail.setTotalAmount(billDetail.getPrice().multiply(BigDecimal.valueOf(billDetail.getQuantity())));
        this.billDetailService.save(billDetail);

        this.setTotalAmount(billDetail.getBill());
        System.out.println("dang o phuong thuc " + billDetailAjax.getMethod());
        this.getUpdateQuantityProduct(productDetail.getId(),quantityUpdate);

        getDeleteVoucherByBill(billDetail.getBill().getId());

        return ResponseEntity.ok(thongBao);
    }
    //THEM BSP BANG QR
    @PostMapping("/addProductByQr")
    public ResponseEntity<Map<String,String>> addProductDetailBuQr(@RequestBody Map<String, String> requestData, HttpSession session) {
        String dataId = requestData.get("id"); // Lấy giá trị từ JSON
        Map<String,String> thongBao = new HashMap<>();
        Bill billById = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(new Bill());

        ProductDetail productDetail = this.billDetailService.getProductDetailById(Integer.parseInt(dataId));

        BillDetail billDetail = getBuyProduct(billById,productDetail,1);

        Integer idBillDetail = this.billDetailService.getBillDetailExist(billById.getId(),productDetail.getId());
        if(idBillDetail != null) {
            thongBao.put("message","Sửa số lượng sản phẩm thành công!");
            thongBao.put("check","1");
        }else {
            thongBao.put("message","Thêm sản phẩm thành công!");
            thongBao.put("check","1");
        }
        this.billDetailService.save(billDetail);
        System.out.println("Thong tin bill: " + billDetail.getBill().toString());

        this.setTotalAmount(billById);
        this.getUpdateQuantityProduct(productDetail.getId(),1);

        getDeleteVoucherByBill(billDetail.getBill().getId());

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
    public List<Object[]> getProductDetailSell(@PathVariable("pageNumber") Integer pageNumber, HttpSession session) {
        Pageable pageable = PageRequest.of(pageNumber-1,4);
        if(this.productDetailCheckMark2Request == null) {
            this.productDetailCheckMark2Request = new ProductDetailCheckMark2Request("",null,null,null,null,null,null,null);
        }
        List<Object[]> productDetails = this.billDetailService.findProductDetailSaleTest(this.productDetailCheckMark2Request,(Integer) session.getAttribute("IdBill"));
        System.out.println("Số lượng 1 trang la " + productDetails.size());
        return convertListToPage(productDetails,pageable).getContent();
    }
    @GetMapping("/image-product/{idProduct}")
    public List<ImageProductResponse> getImageByProduct(@PathVariable("idProduct") Integer idProduct) {
        return this.billDetailService.getImageByBill(idProduct);
    }
    @GetMapping("/category-product/{idProduct}")
    public List<CategoryProductResponse> getCategoryByProduct(@PathVariable("idProduct") Integer idProduct) {
        return this.billDetailService.getCategoryByBill(idProduct);
    }

    @GetMapping("/page-max-product")
    public Integer getMaxPageProduct(HttpSession session) {
        if(this.productDetailCheckMark2Request == null) {
            this.productDetailCheckMark2Request = new ProductDetailCheckMark2Request("",null,null,null,null,null,null,null);
        }
        Integer maxPageProduct = (int) Math.ceil((double) this.billDetailService.findProductDetailSaleTest(this.productDetailCheckMark2Request,(Integer) session.getAttribute("IdBill")).size() / 4);
        System.out.println("so trang cua san pham " + maxPageProduct);
        return maxPageProduct;
    }
//    @PostMapping("/filter-product-deatail")
//    public ResponseEntity<?> getFilterProduct(@RequestBody ProductDetailCheckRequest productDetailCheckRequest2, HttpSession session) {
//        if(productDetailCheckRequest2.getIdCategories().get(0) == 0) {
//            productDetailCheckRequest2.setIdCategories(null);
//        }
//        this.productDetailCheckRequest = productDetailCheckRequest2;
//
//        System.out.println("Thong tin loc " + this.productDetailCheckRequest.toString());
//        return ResponseEntity.ok("Done");
//    }
    @PostMapping("/filter-product-deatail")
    public ResponseEntity<?> getFilterProduct(@RequestBody ProductDetailCheckMark2Request productDetailCheckRequest2, HttpSession session) {
        this.productDetailCheckMark2Request = productDetailCheckRequest2;
        System.out.println("Thong tin loc " + productDetailCheckRequest2.toString());
        return ResponseEntity.ok("Done");
    }


    @GetMapping("/filter-color")
    public List<Color> getFilterColor() {
        return this.colorService.getColorNotStatus0();
    }

    @GetMapping("/filter-size")
    public List<Size> getFilterSize() {
        return this.sizeService.getSizeNotStatus0();
    }

    @GetMapping("/filter-material")
    public List<Material> getFilterMaterial() {
        return this.materialService.getMaterialNotStatus0();
    }

    @GetMapping("/filter-manufacturer")
    public List<Manufacturer> getFilterManufacturer() {
        return this.manufacturerService.getManufacturerNotStatus0();
    }

    @GetMapping("/filter-origin")
    public List<Origin> getFilterOrigin() {
        return this.originService.getOriginNotStatus0();
    }
    @GetMapping("/filter-sole")
    public List<Sole> getFilterSole() {
        return this.soleService.getSoleNotStatus0();
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

    @PostMapping("/update-bill-type")
    public ResponseEntity<?> getUpdateBillType(@RequestBody Map<String, String> map,HttpSession session) {
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(new Bill());
        bill.setUpdateDate(new Date());
        bill.setBillType(Integer.parseInt(map.get("typeBill")));
        this.billService.save(bill);
        return ResponseEntity.ok("Done");
    }

    //danh cho phan quan ly bill

    @GetMapping("/manage-bill/{page}")
    public List<BillResponseManage> getAllBillDistStatus0(@PathVariable("page") Integer page) {
        Pageable pageable = PageRequest.of(page-1,5);
        if(searchBillByStatusRequest == null) {
            searchBillByStatusRequest = new SearchBillByStatusRequest(null);
        }
        System.out.println(searchBillByStatusRequest.getStatusSearch());
        return this.billService.getAllBillByStatusDiss0(keyBillmanage,searchBillByStatusRequest,pageable).getContent();
    }

    @GetMapping("/manage-bill-max-page")
    public Integer getMaxPageBillManage() {
        if(searchBillByStatusRequest == null) {
            searchBillByStatusRequest = new SearchBillByStatusRequest();
        }
        Integer page = (int) Math.ceil((double) this.billService.getAllBillByStatusDiss0(keyBillmanage,searchBillByStatusRequest).size() / 5);
        System.out.println("so trang toi da cua quan ly hoa don " + page);
        return page;
    }

    @PostMapping("/status-bill-manage")
    public ResponseEntity<?> getClickStatusBill(@RequestBody SearchBillByStatusRequest status) {
//        if(status == 999){
//            status = null;
//        }
//        this.statusBillCheck = status;
        System.out.println(status.toString());
        this.searchBillByStatusRequest = status;
        return ResponseEntity.ok("done");
    }

    @PostMapping("/bill-manage-search")
    public ResponseEntity<?> getSearchBillManage(@RequestBody Map<String, String> billSearch) {
        String keyword = billSearch.get("keywordBill");
        this.keyBillmanage = keyword;
        System.out.println("du lieu loc vc " + keyword);
        return ResponseEntity.ok("done");
    }

    //giao dientrnag thia bill
    @GetMapping("/show-status-bill")
    public List<InvoiceStatus> getShowInvoiceStatus(HttpSession session) {
        List<InvoiceStatus> invoiceStatuses = this.invoiceStatusService.getALLInvoiceStatusByBill((Integer) session.getAttribute("IdBill"));
//        session.removeAttribute("idBillCheckStatus");
        return invoiceStatuses;
    }

    @GetMapping("/show-invoice-status-bill")
    public InformationBillByIdBillResponse getInformationBillByIdBill(HttpSession session) {
        InformationBillByIdBillResponse informationBillByIdBillResponse = billService.getInformationBillByIdBill((Integer) session.getAttribute("IdBill"));
        System.out.println(informationBillByIdBillResponse.toString());
        return informationBillByIdBillResponse;
    }
    @GetMapping("/show-customer-in-bill-ship")
    public ClientBillInformationResponse getCustomerInBillShip(HttpSession session) {
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(null);
        if (bill.getCustomer() != null && !bill.getAddRess().equals("Không có")) {
            String getAddRessDetail = bill.getAddRess();
            String[] part = getAddRessDetail.split(",\\s*");
            String fullName = part[0];
            String numberPhone = part[1];
            String province = part[2];
            String district = part[3];
            String ward = part[4];
            String addRessDetail = String.join(", ", java.util.Arrays.copyOfRange(part, 5, part.length));
            ClientBillInformationResponse clientBillInformationResponse = new ClientBillInformationResponse(fullName,numberPhone,bill.getCustomer().getEmail(),province,district,ward,addRessDetail);
            System.out.println("thong tin cua doi tuong ship " + clientBillInformationResponse.toString());
            return clientBillInformationResponse;
        }
        return null;
    }

    @GetMapping("/show-customer-in-bill-not-ship")
    public InfomationCustomerBillResponse getShowCustomerInBillNotShip(HttpSession session) {
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(null);
        if (bill.getCustomer() != null) {
            InfomationCustomerBillResponse info = new InfomationCustomerBillResponse();
            info.setFullName(bill.getCustomer().getFullName());
            info.setEmail(bill.getCustomer().getEmail());
            info.setNumberPhone(bill.getCustomer().getNumberPhone());
            String[] part = bill.getCustomer().getAddRess().split(",\\s*");
            info.setAddRessDetail(String.join(", ", java.util.Arrays.copyOfRange(part, 3, part.length)));
            return info;
        }
        return null;
    }

    @PostMapping("/update-customer-ship")
    public ResponseEntity<Map<String,String>> getUpdateclientBillInformation(@RequestBody ClientBillInformationResponse clientBillInformationResponse,HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(null);
        bill.setUpdateDate(new Date());
        bill.setAddRess(clientBillInformationResponse.getName()+","
                +clientBillInformationResponse.getNumberPhone()+","
                +clientBillInformationResponse.getCity()+","
                +clientBillInformationResponse.getDistrict()+","
                +clientBillInformationResponse.getCommune()+","
                +clientBillInformationResponse.getAddressDetail());
        this.billService.save(bill);
        thongBao.put("message","Sửa thông tin giao hàng thành công!");
        thongBao.put("check","1");
        return ResponseEntity.ok(thongBao);
    }

    @GetMapping("/update-ship-money")
    public ResponseEntity<Map<String,String>> getUpdateShipMoneyBillInformation(String money,HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(null);
        if (bill.getStatus() == 0 ) {
            thongBao.put("message","Loi ne");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        BigDecimal moneyNumber = new BigDecimal(money);
        bill.setUpdateDate(new Date());
        bill.setShippingPrice(moneyNumber);
        this.billService.save(bill);
        System.out.println("da cap nhat lai gia ship");
        thongBao.put("message","Sửa thông tin giao hàng thành công!");
        thongBao.put("check","1");
        return ResponseEntity.ok(thongBao);
    }

    @GetMapping("/confirm-bill/{content}")
    public ResponseEntity<Map<String,String>> getCancelBill(@PathVariable("content") String content,HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();
        Bill bill = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(null);
        if (bill.getStatus() == 0 ) {
            thongBao.put("message","Loi ne");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        if(content.equals("cancel")) {
            bill.setUpdateDate(new Date());
            bill.setStatus(6);
            mess = "Hóa đơn đã được hủy!";
            colorMess = "3";
            this.billService.save(bill);
            this.setBillStatus(bill.getId(),bill.getStatus(),session);
        }else if (content.equals("agree")) {
            if(bill.getStatus() == 4 && bill.getPaymentStatus() == 0) {
                mess = "Đơn hàng chưa được thanh toán!";
                colorMess = "3";
            }else {
                bill.setUpdateDate(new Date());
                bill.setStatus(bill.getStatus()+1);
                mess = "Hóa đơn đã được xác nhận!";
                colorMess = "1";
                if(bill.getStatus() == 2) {
                    getInsertPriceDiscount(bill.getId());
                }
                this.billService.save(bill);
                this.setBillStatus(bill.getId(),bill.getStatus(),session);
            }
        }else if (content.equals("agreeReturnBill")) {
            bill.setUpdateDate(new Date());
            bill.setStatus(8);
            mess = "Hóa đơn đã được xác nhận!";
            colorMess = "1";
            this.billService.save(bill);
            this.setBillStatus(bill.getId(),202,session);
        }else if(content.equals("cancelReturnBill")) {
            bill.setUpdateDate(new Date());
            bill.setStatus(9);
            mess = "Hóa đơn đã được hủy!";
            colorMess = "1";
            this.billService.save(bill);
            this.setBillStatus(bill.getId(),203,session);
        }
        thongBao.put("message",mess);
        thongBao.put("check",colorMess);
        return ResponseEntity.ok(thongBao);
    }

    @PostMapping("/payment-for-ship")
    public ResponseEntity<Map<String,String>> getPaymentForShip(@RequestBody Map<String, String> paymentData,HttpSession session, HttpServletRequest request) {
        Map<String,String> thongBao = new HashMap<>();
        String cashPay = paymentData.get("cashPay");
        String cashAcountPay = paymentData.get("cashAcountPay");
        String cashBillPay = paymentData.get("cashBillPay");
        String notePay = paymentData.get("notePay");
        String payStatus = paymentData.get("payStatus");
        String surplusMoneyPay = paymentData.get("surplusMoneyPay");
        String payMethod = paymentData.get("payMethod");
        System.out.println("Du lieu khi thnah toan ");

        System.out.println("cashPay: " + cashPay);
        System.out.println("cashAcountPay: " + cashAcountPay);
        System.out.println("cashBillPay: " + cashBillPay);
        System.out.println("notePay: " + notePay);
        System.out.println("payStatus: " + payStatus);
        System.out.println("surplusMoneyPay: " + surplusMoneyPay);
        System.out.println("payMethod: " + payMethod);
        mess = "Thanh toan thanh cong";
        colorMess = "1";
        Integer checkPayMethod = Integer.parseInt(payMethod);
        if(checkPayMethod == 1) {
            Bill billPayment = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(null);
            BigDecimal cash = BigDecimal.valueOf(Long.parseLong(cashPay)).subtract(BigDecimal.valueOf(Long.parseLong(surplusMoneyPay)));
            billPayment.setCash(cash);
            if(billPayment.getNote().trim().equals("")) {
                billPayment.setNote("Thanh toán bằng tiền mặt!");
            }
            if(notePay.trim().equals("")) {
                session.setAttribute("notePayment","Thanh toán bằng tiền mặt!");
            }else {
                session.setAttribute("notePayment",notePay);
            }
            billPayment.setPaymentMethod(checkPayMethod);
            billPayment.setPaymentStatus(Integer.parseInt(payStatus));
            billPayment.setUpdateDate(new Date());
            billPayment.setSurplusMoney(BigDecimal.valueOf(Long.parseLong(surplusMoneyPay)));
            this.billService.save(billPayment);
            thongBao.put("message",mess);
            thongBao.put("check",colorMess);
            this.setBillStatus(billPayment.getId(),101,session);
            return ResponseEntity.ok(thongBao);
        }else {
            Bill billPayment = this.billService.findById((Integer) session.getAttribute("IdBill")).orElse(null);
            billPayment.setPaymentMethod(checkPayMethod);
            if(notePay.trim().equals("") && checkPayMethod == 2) {
                notePay = "Thanh toán bằng tiền tài khoản!";
                session.setAttribute("notePayment",notePay);
            }else {
                notePay = "Thanh toán bằng tiền tài khoan và tiền mặt!";
                billPayment.setCash(BigDecimal.valueOf(Long.parseLong(cashPay)));
                session.setAttribute("notePayment",notePay);
            }
            if(billPayment.getNote().trim().equals("")) {
                billPayment.setNote(notePay);
            }
            session.setAttribute("billPaymentRest",billPayment);
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnPayService.createOrder((Integer.parseInt(cashAcountPay)), "chuyenKhoan", baseUrl);
            thongBao.put("vnpayUrl",vnpayUrl);
            return ResponseEntity.ok(thongBao);
        }
    }
    @GetMapping("/infomation-payment-bill")
    public List<Object[]> getInfomationPaymentByIdBill(HttpSession session) {
        List<Object[]> results = billService.getInfoPaymentByIdBill((Integer) session.getAttribute("IdBill"));
        return results;
    }
    @GetMapping("/infomation-history-bill")
    public List<Object[]> getInfomationHistoryByIdBill(HttpSession session) {
        List<Object[]> results = invoiceStatusService.getHistoryByBill((Integer) session.getAttribute("IdBill"));
        for (Object[] row : results) {
            // Duyệt qua các phần tử trong từng Object[] và in ra giá trị
            System.out.println("Row data: ");
            for (Object obj : row) {
                System.out.print(obj + " "); // In từng giá trị trong Object[]
            }
            System.out.println(); // Xuống dòng sau khi in hết một hàng
        }
        return results;
    }

//    @GetMapping("/download-filled-pdf")
//    public ResponseEntity<byte[]> downloadFilledPdf() throws Exception {
//        List<Object[]> productList = new ArrayList<>();
//        productList.add(new Object[]{"Giày Jordan 1", 1, 2500000.0, 2500000.0});
//        productList.add(new Object[]{"Giày Yeezy Boost 350", 2, 6000000.0, 12000000.0});
//        productList.add(new Object[]{"Giày Asics Gel-Lyte III", 3, 850000.0, 2550000.0});
//        productList.add(new Object[]{"Giày Jordan 1", 1, 2500000.0, 2500000.0});
//        productList.add(new Object[]{"Giày Yeezy Boost 350", 2, 6000000.0, 12000000.0});
//        productList.add(new Object[]{"Giày Asics Gel-Lyte III", 3, 850000.0, 2550000.0});
//        // Tạo file PDF từ danh sách sản phẩm
//        ByteArrayOutputStream pdfStream = pdfTemplateService.fillPdfTemplate(productList);
//        byte[] pdfBytes = pdfStream.toByteArray();
//        // Đường dẫn nơi file PDF sẽ được lưu trên ổ đĩa
//        String filePath = "D:/hd2.pdf";  // Cập nhật đường dẫn theo ổ đĩa của bạn
//
////        // Ghi file PDF vào ổ đĩa
////        try (FileOutputStream fos = new FileOutputStream(filePath)) {
////            fos.write(pdfBytes);
////            fos.flush();
////        } catch (IOException e) {
////            e.printStackTrace();  // Xử lý lỗi nếu có
////        }
//
//        // Trả về file PDF dưới dạng download cho người dùng
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDispositionFormData("attachment", "hd1.pdf");
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(pdfBytes);
//    }

    @GetMapping("/bill-pdf/{idBill}")
    public ResponseEntity<byte[]> getBillDetails(@PathVariable("idBill") Integer idBill) throws Exception {
        // Lấy chi tiết hóa đơn và thông tin hóa đơn từ service
        List<Object[]> billDetails = this.billService.getBillDetailByIdBillPDF(idBill);
        List<Object[]> billInfoList = this.billService.getBillByIdCreatePDF(idBill);

        // Kiểm tra dữ liệu
        if (billInfoList == null || billDetails.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        Object[] billInfo = billInfoList.get(0);

        // Tạo PDF từ template
        ByteArrayOutputStream pdfStream = pdfTemplateService.fillPdfTemplate(billInfo, billDetails);
        byte[] pdfBytes = pdfStream.toByteArray();

        // Đường dẫn thư mục và file
        String directoryPath = "D:/danhSachHoaDon/";
        String filePath = directoryPath + billInfo[0] + ".pdf";

        // Kiểm tra và tạo thư mục nếu chưa tồn tại
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();  // Tạo thư mục
        }

        // Lưu file PDF
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfBytes);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();  // Xử lý lỗi nếu có
            return ResponseEntity.status(500).body(null);  // Trả về mã lỗi 500 nếu xảy ra lỗi khi ghi file
        }

        // Trả về phản hồi thành công
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", billInfo[0] + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/list-product-sell-test")
    public List<ProductDetailSellResponse> productDetailSellResponses(HttpSession session) {
        this.productDetailCheckRequest = new ProductDetailCheckRequest("",null,null,null,null,null,null);
        if(this.productDetailCheckMark2Request == null) {
            this.productDetailCheckMark2Request = new ProductDetailCheckMark2Request("",null,null,null,null,null,null,null);
        }
        Integer i = 1;
        System.out.println("cai nay cua ban test: ");
//        ProductDetailCheckMark2Request request = new ProductDetailCheckMark2Request();
//        // Đặt danh sách id màu sắc
//        request.setIdColors(new Integer[]{1, 2});
//
//        // Đặt danh sách id kích thước
//        request.setIdSizes(null);
//
//        // Đặt danh sách id chất liệu
//        request.setIdMaterials(null);
//
//        // Đặt danh sách id nhà sản xuất
//        request.setIdManufacturers(null);
//
//        // Đặt danh sách id nơi xuất xứ
//        request.setIdOrigins(null);
//
//        // Đặt danh sách id đế giày
//        request.setIdSoles(null);
//
//        // Đặt danh sách id danh mục
//        request.setIdCategories(null);

        // In ra thông tin của request để kiểm tra
        for (Object[] objects: this.billDetailService.findProductDetailSaleTest(this.productDetailCheckMark2Request,(Integer) session.getAttribute("IdBill"))) {
            System.out.println("Sản phẩm thứ " + i);
            System.out.println("Sản phẩm có id là: " + objects[0]);  // pd.id
            System.out.println("Tên sản phẩm: " + objects[1]);  // p.name_product
            System.out.println("Tên màu: " + objects[2]);  // c.name_color
            System.out.println("Tên size: " + objects[3]);  // s.name_size
            System.out.println("Nhà sản xuất: " + objects[4]);  // m.name_manufacturer
            System.out.println("Chất liệu: " + objects[5]);  // mat.name_material
            System.out.println("Xuất xứ: " + objects[6]);  // o.name_origin
            System.out.println("Loại đế: " + objects[7]);  // so.name_sole
            System.out.println("-----------------------------------------");
            i++;
        }
//        System.out.println("cai nay cua ban chinh thuc: ");
////        Integer n = 1;
////        for (ProductDetail productDetail: this.billDetailService.getProductDetailSale(productDetailCheckRequest)) {
////            System.out.println("san pham "+n+" co id la: "+productDetail.getId());
////            n++;
////        }
        return null;
    }


}