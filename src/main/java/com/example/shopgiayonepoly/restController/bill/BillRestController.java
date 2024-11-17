package com.example.shopgiayonepoly.restController.bill;

import com.example.shopgiayonepoly.baseMethod.BaseBill;
import com.example.shopgiayonepoly.baseMethod.BaseEmail;
import com.example.shopgiayonepoly.baseMethod.BaseProduct;
import com.example.shopgiayonepoly.dto.request.bill.*;
import com.example.shopgiayonepoly.dto.response.bill.*;
import com.example.shopgiayonepoly.entites.*;
import com.example.shopgiayonepoly.entites.baseEntity.Base;
import com.example.shopgiayonepoly.service.EmailSenderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("/bill-api")
public class BillRestController extends BaseBill {
    @GetMapping("/get-idbill")
    @ResponseBody
    public Integer getIdBillFromSession(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return -1;
        }
        return (Integer) session.getAttribute("IdBill");
    }

    @GetMapping("/all")
    public List<Bill> getAll(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        return billService.findAll();
    }

    @GetMapping("/all-new")
    public List<Bill> getAllNew(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        keyVoucher = "";
        productDetailCheckRequest = new ProductDetailCheckRequest("",null,null,null,null,null,null);
        Pageable pageable = PageRequest.of(0,10);
        return billService.getBillByStatusNew(pageable);
    }

    @GetMapping("/bill-detail-by-id-bill/{numberPage}")
    public List<BillDetail> getBillDetail(@PathVariable("numberPage") String pageNumber,HttpSession session) {

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");
        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            return null;
        }
        Bill bill = this.billService.findById(idBill).orElse(null);
        if (bill == null || bill.getId() == null) {
            return null;
        }
        String validatePagenumber = validateInteger(pageNumber);
        if (!validatePagenumber.trim().equals("")) {
            return null;
        }

        Pageable pageable = PageRequest.of(Integer.parseInt(pageNumber) - 1, 2);
        keyVoucher = "";
        productDetailCheckRequest = new ProductDetailCheckRequest("",null,null,null,null,null,null);
        // Lấy danh sách BillDetail
        List<BillDetail> billDetailList = this.billDetailService.getBillDetailByIdBill(idBill, pageable).getContent();
        if(bill.getStatus() == 0) {
            getDeleteVoucherByBill(idBill);
        }
        return billDetailList;
    }

    //cai nay dung de lam nut tang gia, so luong
    @PostMapping("/updateBillDetail")
    public ResponseEntity<Map<String,String>> getUpdateBillDetail(@RequestBody BillDetailAjax billDetailAjax,HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");

        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        BillDetail billDetail = this.billDetailService.findById(billDetailAjax.getId()).orElse(null);
        if (billDetail == null) {
            thongBao.put("message","Sản phẩm mua không tồn tại!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        System.out.println("id billdetail la: " + billDetail.getId());
        System.out.println("so luong moi la: " + billDetailAjax.getQuantity());
        ProductDetail productDetail   = this.billDetailService.getProductDetailById(billDetail.getProductDetail().getId());
        Bill bill = billDetail.getBill();
        if(bill == null || bill.getId() == null || bill.getStatus() > 2 || bill.getPaymentStatus() == 1) {
            thongBao.put("message","Không thể điều chỉnh số lượng mua!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        if(!billDetailAjax.getMethod().equals("cong") && !billDetailAjax.getMethod().equals("tru")) {
            thongBao.put("message","Sai phương thức!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
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
            thongBao.put("message","Số lượng mua mỗi món không được quá 10!");
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
        if(bill.getStatus() == 0) {
            this.getUpdateQuantityProduct(productDetail.getId(),quantityUpdate);
        }

        if(billDetail.getBill().getStatus() == 0) {
            getDeleteVoucherByBill(billDetail.getBill().getId());
        }

        return ResponseEntity.ok(thongBao);
    }
    //THEM BSP BANG QR
    @PostMapping("/addProductByQr")
    public ResponseEntity<Map<String,String>> addProductDetailBuQr(@RequestBody Map<String, String> requestData, HttpSession session) {
        String dataId = requestData.get("id"); // Lấy giá trị từ JSON
        Map<String,String> thongBao = new HashMap<>();

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);

        }

        Integer idBill = (Integer) session.getAttribute("IdBill");

        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            this.mess = "Sai định dạng hóa đơn!";
            this.colorMess = "3";
            return ResponseEntity.ok(thongBao);
        }

        Bill billById = this.billService.findById(idBill).orElse(null);
        if (billById == null) {
            this.mess = "Hóa đơn không tồn tại!";
            this.colorMess = "3";
            return ResponseEntity.ok(thongBao);
        }

        if (billById.getPaymentStatus() == 1) {
            this.mess = "Hóa đã thanh toán!";
            this.colorMess = "3";
            return ResponseEntity.ok(thongBao);
        }

        ProductDetail productDetail = this.billDetailService.getProductDetailById(Integer.parseInt(dataId));
        if(productDetail == null) {
            this.mess = "Sản phẩm không tồn tại!";
            this.colorMess = "3";
            return ResponseEntity.ok(thongBao);
        }

        BillDetail billDetail = getBuyProduct(billById,productDetail,1);

        Integer idBillDetail = this.billDetailService.getBillDetailExist(billById.getId(),productDetail.getId());
        if(idBillDetail != -1) {
            thongBao.put("message","Sửa số lượng sản phẩm thành công!");
            thongBao.put("check","1");
        }else {
            thongBao.put("message","Thêm sản phẩm thành công!");
            thongBao.put("check","1");
        }
        this.billDetailService.save(billDetail);
        System.out.println("Thong tin bill: " + billDetail.getBill().toString());

        this.setTotalAmount(billById);
        Bill bill = billDetail.getBill();
        if(bill.getStatus()  == 1 || bill.getStatus() == 0) {
            this.getUpdateQuantityProduct(productDetail.getId(),1);
        }

        if(billDetail.getBill().getStatus() == 0) {
            getDeleteVoucherByBill(billDetail.getBill().getId());
        }

        return ResponseEntity.ok(thongBao);
    }


    @GetMapping("/allProductDetail")
    public List<ProductDetail> getAllProductDetail(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        return this.billDetailService.getAllProductDetail();
    }

    //
    //PhanTrang
    @GetMapping("/max-page-billdetail")
    public Integer numberPage(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        Integer idBill = (Integer) session.getAttribute("IdBill");
        Integer pageNumber = (int) Math.ceil((double) this.billDetailService.getBillDetailByIdBill(idBill).size() / 2);
        System.out.println("Số trang là: " + pageNumber);
        return pageNumber;
    }

    @GetMapping("/productDetail-sell/{pageNumber}")
    public List<Object[]> getProductDetailSell(@PathVariable("pageNumber") String pageNumber, HttpSession session) {
        String validatePageNumber= validateInteger(pageNumber);
        if(!validatePageNumber.trim().equals("")) {
            return null;
        }
        Pageable pageable = PageRequest.of(Integer.parseInt(pageNumber)-1,4);

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(this.productDetailCheckMark2Request == null) {
            this.productDetailCheckMark2Request = new ProductDetailCheckMark2Request("",null,null,null,null,null,null,null);
        }
        List<Object[]> productDetails = this.billDetailService.findProductDetailSaleTest(this.productDetailCheckMark2Request,(Integer) session.getAttribute("IdBill"));
        System.out.println("Số lượng 1 trang la " + productDetails.size());
        return convertListToPage(productDetails,pageable).getContent();
    }
    @GetMapping("/image-product/{idProduct}")
    public List<ImageProductResponse> getImageByProduct(@PathVariable("idProduct") Integer idProduct,HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        return this.billDetailService.getImageByBill(idProduct);
    }
    @GetMapping("/category-product/{idProduct}")
    public List<CategoryProductResponse> getCategoryByProduct(@PathVariable("idProduct") Integer idProduct,HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        return this.billDetailService.getCategoryByBill(idProduct);
    }

    @GetMapping("/page-max-product")
    public Integer getMaxPageProduct(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(this.productDetailCheckMark2Request == null) {
            this.productDetailCheckMark2Request = new ProductDetailCheckMark2Request("",null,null,null,null,null,null,null);
        }
        Integer idBill = (Integer) session.getAttribute("IdBill");

        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            return null;
        }
        Integer maxPageProduct = (int) Math.ceil((double) this.billDetailService.findProductDetailSaleTest(this.productDetailCheckMark2Request,idBill).size() / 4);
        System.out.println("so trang cua san pham " + maxPageProduct);
        return maxPageProduct;
    }

    @PostMapping("/filter-product-deatail")
    public ResponseEntity<?> getFilterProduct(@RequestBody ProductDetailCheckMark2Request productDetailCheckRequest2, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        this.productDetailCheckMark2Request = productDetailCheckRequest2;
        System.out.println("Thong tin loc " + productDetailCheckRequest2.toString());
        return ResponseEntity.ok("Done");
    }


    @GetMapping("/filter-color")
    public List<Color> getFilterColor(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        return this.colorService.getColorNotStatus0();
    }

    @GetMapping("/filter-size")
    public List<Size> getFilterSize(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        return this.sizeService.getSizeNotStatus0();
    }

    @GetMapping("/filter-material")
    public List<Material> getFilterMaterial(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        return this.materialService.getMaterialNotStatus0();
    }

    @GetMapping("/filter-manufacturer")
    public List<Manufacturer> getFilterManufacturer(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        return this.manufacturerService.getManufacturerNotStatus0();
    }

    @GetMapping("/filter-origin")
    public List<Origin> getFilterOrigin(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        return this.originService.getOriginNotStatus0();
    }
    @GetMapping("/filter-sole")
    public List<Sole> getFilterSole(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        return this.soleService.getSoleNotStatus0();
    }

    //goi khach hang
    @GetMapping("/client")
    public List<Customer> getClient(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        Pageable pageable = PageRequest.of(0,5);
        return this.billService.getClientNotStatus0();
    }

    @GetMapping("/payment-information")
    public BillTotalInfornationResponse billTotalInfornationResponse(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        Integer idBill = (Integer) session.getAttribute("IdBill");

        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            return null;
        }
        return this.billService.findBillVoucherById(idBill);
    }

    @GetMapping("/client-bill-information")
    public ClientBillInformationResponse getClientBillInformation(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        List<ClientBillInformationResponse> clientBillInformationResponses = this.billService.getClientBillInformationResponse((Integer) session.getAttribute("IdClient"));
        if(clientBillInformationResponses.size() < 0) {
            return null;
        }
        ClientBillInformationResponse clientBillInformationResponse = clientBillInformationResponses.get(0);
        if (clientBillInformationResponses == null) {
            return null;
        }
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
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");

        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            return null;
        }

        Bill bill = this.billService.findById(idBill).orElse(null);
        if(bill == null) {
            return null;
        }
        if(payMethodRequest.getPayMethod() > 3) {
            return null;
        }
        bill.setPaymentMethod(payMethodRequest.getPayMethod());
        bill.setUpdateDate(new Date());
        bill.setStaff(staffLogin);
        return this.billService.save(bill);
    }

    @GetMapping("/voucher/{page}")
    public List<Object[]> getVouCherList(@PathVariable("page") String pageNumber,HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        Integer idBill = (Integer) session.getAttribute("IdBill");

        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            return null;
        }
        Pageable pageable = PageRequest.of(Integer.parseInt(pageNumber)-1,5);
        System.out.println("da loc duoc " + this.keyVoucher);
        System.out.println(idBill);
        Page<Voucher> vouchers = this.billService.getVouCherByBill(idBill,keyVoucher,pageable);
        return this.convertListToPage(this.billService.getVoucherByBillV2(idBill,keyVoucher),pageable).getContent();
//        return vouchers.getContent();
    }

    @PostMapping("/voucher-search")
    public ResponseEntity<?> getSearchVoucher(@RequestBody Map<String, String> voucherSearch,HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        String keyword = voucherSearch.get("keyword");
        this.keyVoucher = keyword;
        System.out.println("du lieu loc vc " + voucherSearch);
        return ResponseEntity.ok("Done v");
    }

    @GetMapping("/max-page-voucher")
    public Integer getMaxPageVoucher(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        Integer idBill = (Integer) session.getAttribute("IdBill");

        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            return null;
        }
        System.out.println("id bill vung page voucgher " + idBill);

        // Đảm bảo danh sách không bị null
//        List<Voucher> vouchers = this.billService.getVoucherByBill(idBill,this.keyVoucher);
        List<Object[]> voucher = this.billService.getVoucherByBillV2(idBill,keyVoucher);
        Integer maxPage = (int) Math.ceil((double) voucher.size() / 5);
        System.out.println("Số trang tối đa của voucher: " + maxPage);
        return maxPage;
    }

    @GetMapping("/categoryAll")
    public List<Category> getAllCategory(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        return this.billDetailService.getAllCategores();
    }

    @PostMapping("/update-bill-type")
    public ResponseEntity<?> getUpdateBillType(@RequestBody Map<String, String> map,HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        Integer idBill = (Integer) session.getAttribute("IdBill");

        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            return null;
        }
        Bill bill = this.billService.findById(idBill).orElse(null);
        if(bill == null) {
            return null;
        }

        if(Integer.parseInt(map.get("typeBill")) > 2) {
            return null;
        }
        bill.setUpdateDate(new Date());
        bill.setBillType(Integer.parseInt(map.get("typeBill")));
        this.billService.save(bill);
        return ResponseEntity.ok("Done");
    }

    //danh cho phan quan ly bill

    @GetMapping("/manage-bill/{page}")
    public List<BillResponseManage> getAllBillDistStatus0(@PathVariable("page") String page,HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }

        String validatePage = validateInteger(page);
        if(!validatePage.trim().equals("")) {
           return null;
        }

        Pageable pageable = PageRequest.of(Integer.parseInt(page)-1,5);
        if(searchBillByStatusRequest == null) {
            searchBillByStatusRequest = new SearchBillByStatusRequest(null);
        }
        System.out.println(searchBillByStatusRequest.getStatusSearch());
        return this.billService.getAllBillByStatusDiss0(keyBillmanage,searchBillByStatusRequest,keyStartDate,keyEndDate,pageable).getContent();
    }

    @GetMapping("/manage-bill-max-page")
    public Integer getMaxPageBillManage(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(searchBillByStatusRequest == null) {
            searchBillByStatusRequest = new SearchBillByStatusRequest();
        }
        Integer page = (int) Math.ceil((double) this.billService.getAllBillByStatusDiss0(keyBillmanage,searchBillByStatusRequest,keyStartDate,keyEndDate).size() / 5);
        System.out.println("so trang toi da cua quan ly hoa don " + page);
        return page;
    }

    @PostMapping("/status-bill-manage")
    public ResponseEntity<?> getClickStatusBill(@RequestBody SearchBillByStatusRequest status,HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        System.out.println(status.toString());
        this.searchBillByStatusRequest = status;
        return ResponseEntity.ok("done");
    }

    @PostMapping("/bill-manage-search")
    public ResponseEntity<?> getSearchBillManage(@RequestBody Map<String, String> billSearch,HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        String keyword = billSearch.get("keywordBill");
        this.keyBillmanage = keyword;
        String startDateStr = billSearch.get("startDate");
        String endDateStr = billSearch.get("endDate");

        System.out.println("du lieu loc vc " + keyword);
        System.out.println("starDate-bill-manage: "+billSearch.get("startDate"));
        System.out.println("endDate-bill-manage: "+billSearch.get("endDate"));
        try {
            // Định dạng để parse chuỗi thành đối tượng Date
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();

            // Chuyển đổi chuỗi startDateStr thành Date và đặt thời gian bắt đầu của ngày
            Date startDate = formatter.parse(startDateStr);
            calendar.setTime(startDate);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            this.keyStartDate = calendar.getTime();

            // Chuyển đổi chuỗi endDateStr thành Date và đặt thời gian kết thúc của ngày
            Date endDate = formatter.parse(endDateStr);
            calendar.setTime(endDate);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            this.keyEndDate = calendar.getTime();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format. Please use 'yyyy-MM-dd'.");
        }
        return ResponseEntity.ok("done");
    }

    //giao dientrnag thia bill
    @GetMapping("/show-status-bill")
    public List<InvoiceStatus> getShowInvoiceStatus(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        Integer idBill = (Integer) session.getAttribute("IdBill");

        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
           return null;
        }
        List<InvoiceStatus> invoiceStatuses = this.invoiceStatusService.getALLInvoiceStatusByBill(idBill);
        if(invoiceStatuses.size() < 0) {
            return null;
        }
//        session.removeAttribute("idBillCheckStatus");
        return invoiceStatuses;
    }

    @GetMapping("/show-invoice-status-bill")
    public InformationBillByIdBillResponse getInformationBillByIdBill(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");

        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            return null;
        }
        InformationBillByIdBillResponse informationBillByIdBillResponse = billService.getInformationBillByIdBill(idBill);
        if(informationBillByIdBillResponse == null) {
            return null;
        }
        System.out.println(informationBillByIdBillResponse.toString());
        return informationBillByIdBillResponse;
    }
    @GetMapping("/show-customer-in-bill-ship")
    public ClientBillInformationResponse getCustomerInBillShip(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");

        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            return null;
        }
        Bill bill = this.billService.findById(idBill).orElse(null);
        if (bill == null) {
            return null;
        }

        if (!bill.getAddRess().equals("Không có")) {
            String getAddRessDetail = bill.getAddRess();
            String[] part = getAddRessDetail.split(",\\s*");
            String fullName = part[0];
            String numberPhone = part[1];
            String email = part[2];
            String province = part[3];
            String district = part[4];
            String ward = part[5];
            String addRessDetail = String.join(", ", java.util.Arrays.copyOfRange(part, 6, part.length));
            ClientBillInformationResponse clientBillInformationResponse = new ClientBillInformationResponse(fullName,numberPhone,email,province,district,ward,addRessDetail);
            System.out.println("thong tin cua doi tuong ship " + clientBillInformationResponse.toString());
            return clientBillInformationResponse;
        }
        return null;
    }

    @GetMapping("/show-customer-in-bill-not-ship")
    public InfomationCustomerBillResponse getShowCustomerInBillNotShip(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");

        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            return null;
        }
        Bill bill = this.billService.findById(idBill).orElse(null);
        if (bill == null) {
            return null;
        }
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
        String regexNameCustomer = "[\\p{L}\\p{Nd}\\s]+";

        if(
                clientBillInformationResponse.getName().trim().equals("")
                        || clientBillInformationResponse.getName().trim() == null
                        || clientBillInformationResponse.getName().trim().length() > 50
                        || !clientBillInformationResponse.getName().trim().matches(regexNameCustomer)
        ) {
            thongBao.put("message","Tên người nhận hàng không đúng định dạng!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        String regexNumberPhone = "^(0?)(3[2-9]|5[689]|7[06-9]|8[1-6]|9[0-46-9])[0-9]{7}$";
        if (!clientBillInformationResponse.getNumberPhone().trim().matches(regexNumberPhone)) {
            thongBao.put("message","Đây không phải số điện thoại hỗ trợ trong nước!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }


        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");
        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            thongBao.put("message","Sai định dạng hóa đơn!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        Bill bill = this.billService.findById(idBill).orElse(null);
        if(bill == null) {
            thongBao.put("message","Hóa đơn không tồn tại!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        if(bill.getStatus() >= 2 || bill.getPaymentStatus() == 1) {
            thongBao.put("message","Không thể đổi được thng tin giao hàng hóa đơn này!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        bill.setUpdateDate(new Date());
        bill.setAddRess(clientBillInformationResponse.getName()+","
                +clientBillInformationResponse.getNumberPhone()+","
                +clientBillInformationResponse.getEmail()+","
                +clientBillInformationResponse.getCity()+","
                +clientBillInformationResponse.getDistrict()+","
                +clientBillInformationResponse.getCommune()+","
                +clientBillInformationResponse.getAddressDetail());
        bill.setStaff(staffLogin);
        this.billService.save(bill);
        thongBao.put("message","Sửa thông tin giao hàng thành công!");
        thongBao.put("check","1");
        return ResponseEntity.ok(thongBao);
    }

    @GetMapping("/update-ship-money")
    public ResponseEntity<Map<String,String>> getUpdateShipMoneyBillInformation(String money,HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");
        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            thongBao.put("message","Sai định dạng hóa đơn!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        Bill bill = this.billService.findById(idBill).orElse(null);
        if(bill == null) {
            thongBao.put("message","Hóa đơn không tồn tại!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if (bill.getStatus() != 1) {
            thongBao.put("message","Loi");
            thongBao.put("check","3");
            System.out.println("khong duoc cap nhat tien ship");
            return ResponseEntity.ok(thongBao);
        }

        String validateMoneyShip = validateBigDecimal(money);
        if(!validateMoneyShip.trim().equals("")) {
            thongBao.put("message","Sai định dạng phí giao hàng!");
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

    @GetMapping("/confirm-bill/{content}/{reasonConfirm}")
    public ResponseEntity<Map<String,String>> getCancelBill(@PathVariable("content") String content,@PathVariable("reasonConfirm") String reasonConfirm,HttpSession session) {
        Map<String,String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        session.setAttribute("notePayment",reasonConfirm.equals("trong") ? "" : reasonConfirm);
        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");
        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            thongBao.put("message","Sai định dạng hóa đơn!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        Bill bill = this.billService.findById(idBill).orElse(null);
        if(bill == null) {
            thongBao.put("message","Hóa đơn không tồn tại!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if (bill.getStatus() == 0 ) {
            thongBao.put("message","Loi");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        if(content.equals("cancel")) {
            Bill billSave = bill;
            if(billSave.getStatus() >= 2) {
                for (BillDetail billDetail: this.billDetailService.findAll()) {
                    if(billDetail.getBill().getId() == billSave.getId()) {
                        this.getUpdateQuantityProduct(billDetail.getProductDetail().getId(),-(billDetail.getQuantity()));
                    }
                }
            }

            if(billSave.getStatus() == 0 || billSave.getStatus() > 5) {
                thongBao.put("message","Hóa đơn không hợp lệ!");
                thongBao.put("check","3");
                return ResponseEntity.ok(thongBao);
            }

            bill.setUpdateDate(new Date());
            bill.setStatus(6);
            mess = "Hóa đơn đã được hủy!";
            colorMess = "3";
            this.billService.save(bill);

            String getAddRessDetail = bill.getAddRess();
            String[] part = getAddRessDetail.split(",\\s*");
            String fullName = part[0];
            String numberPhone = part[1];
            String email = part[2];
            String province = part[3];
            String district = part[4];
            String ward = part[5];
            String ht = "http://localhost:8080/onepoly/status-bill/"+bill.getId();
            String title = "Đơn hàng đã bị hủy";
            this.templateEmailConfigmBill(email,ht,bill.getCodeBill(),title);
            if (billSave.getVoucher() != null) {
                this.getSubtractVoucher(billSave.getVoucher(),-1);
            }
            this.setBillStatus(bill.getId(),bill.getStatus(),session);
        }else if (content.equals("agree")) {
            if(bill.getStatus() == 4 && bill.getPaymentStatus() == 0) {
                mess = "Đơn hàng chưa được thanh toán!";
                colorMess = "3";
            }else {
                if(bill.getVoucher() != null) {
                    if(bill.getVoucher().getPricesApply().compareTo(bill.getTotalAmount()) > 0) {
                        thongBao.put("message","Mời đổi mã giảm giá trước khi xác nhận đơn hàng!");
                        thongBao.put("check","3");
                        return ResponseEntity.ok(thongBao);
                    }
                }
                if (bill.getStatus() == 0 || bill.getStatus() > 5) {
                    thongBao.put("message","Hóa đơn không hợp lệ!");
                    thongBao.put("check","3");
                    return ResponseEntity.ok(thongBao);
                }
                if(bill.getStatus() == 1) {
                    for (BillDetail billDetail: this.billDetailService.findAll()) {
                        if(billDetail.getBill().getId() == bill.getId()) {
                            for (ProductDetail productDetail: this.productDetailService.findAll()) {
                                if (productDetail.getId() == billDetail.getProductDetail().getId()) {
                                    if((productDetail.getQuantity()-billDetail.getQuantity()) < 0) {
                                        System.out.println(productDetail.toString());
                                        System.out.println(productDetail.getProduct());
                                        System.out.println((productDetail.getQuantity()-billDetail.getQuantity())+"khong hop le");
                                        thongBao.put("message","Hóa đơn không hợp lệ!");
                                        thongBao.put("check","3");
                                        return ResponseEntity.ok(thongBao);
                                    }
                                }
                            }
                        }
                    }
                }
                String getAddRessDetail = bill.getAddRess();
                String[] part = getAddRessDetail.split(",\\s*");
                String fullName = part[0];
                String numberPhone = part[1];
                String email = part[2];
                String province = part[3];
                String district = part[4];
                String ward = part[5];
                String addRessDetail = String.join(", ", java.util.Arrays.copyOfRange(part, 6, part.length));
                System.out.println("email de gui xac nhan " + email);
                if(bill.getStatus() == 1) {
                    String ht = "http://localhost:8080/onepoly/status-bill/"+bill.getId();
                    System.out.println(ht);
                    String title = "Đơn hàng đã được xác nhận";
                    this.templateEmailConfigmBill(email,ht,bill.getCodeBill(),title);
                }
                bill.setUpdateDate(new Date());
                bill.setStatus(bill.getStatus()+1);

                if(bill.getStatus() == 2) {
                    getInsertPriceDiscount(bill.getId());
                }
                Bill billSave = this.billService.save(bill);
                if(billSave.getStatus() == 2) {
                    for (BillDetail billDetail: this.billDetailService.findAll()) {
                        if(billDetail.getBill().getId() == billSave.getId()) {
                            this.getUpdateQuantityProduct(billDetail.getProductDetail().getId(),billDetail.getQuantity());
                        }
                    }
                }

                this.setBillStatus(bill.getId(),bill.getStatus(),session);
            }
        }else if (content.equals("agreeReturnBill")) {
            if (bill.getStatus() != 7) {
                thongBao.put("message","Hóa đơn không hợp lệ!");
                thongBao.put("check","3");
                return ResponseEntity.ok(thongBao);
            }

            ReturnBillExchangeBill returnBillExchangeBill = this.returnBillService.getReturnBillByIdBill(bill.getId());
            if(returnBillExchangeBill != null) {
                if(returnBillExchangeBill.getId() != null) {
                    String checkPay = this.returnBillService.getStatusPaymentorNotPay(returnBillExchangeBill.getId());
                    List<PaymentExchange> paymentExchanges = this.paymentExchangeService.getPaymentExchangeByIdBillExchange(returnBillExchangeBill.getId());

                    if(checkPay.equals("PhaiTraTien") && paymentExchanges.isEmpty()) {
                        thongBao.put("message","Phải thanh toán trước!");
                        thongBao.put("check","3");
                        return ResponseEntity.ok(thongBao);
                    }
                }
            }

            bill.setUpdateDate(new Date());
            bill.setStatus(8);
            mess = "Hóa đơn đã được xác nhận!";
            colorMess = "1";
            this.billService.save(bill);
            this.setBillStatus(bill.getId(),202,session);
        }else if(content.equals("cancelReturnBill")) {
            if (bill.getStatus() != 7) {
                thongBao.put("message","Hóa đơn không hợp lệ!");
                thongBao.put("check","3");
                return ResponseEntity.ok(thongBao);
            }
            bill.setUpdateDate(new Date());
            bill.setStatus(9);
            mess = "Hóa đơn đã được hủy!";
            colorMess = "1";
            Bill billSave = this.billService.save(bill);
            for (ExchangeBillDetail exchangeBillDetail: this.exchangeBillDetailService.findAll()) {
                if(exchangeBillDetail.getExchangeBill().getBill().getId() == billSave.getId()) {
                    this.getUpdateQuantityProduct(exchangeBillDetail.getProductDetail().getId(),-(exchangeBillDetail.getQuantityExchange()));
                }
            }
            this.setBillStatus(bill.getId(),203,session);
        }
        thongBao.put("message",mess);
        thongBao.put("check",colorMess);
        return ResponseEntity.ok(thongBao);
    }

    @PostMapping("/payment-for-ship")
    public ResponseEntity<Map<String,String>> getPaymentForShip(@RequestBody Map<String, String> paymentData,HttpSession session, HttpServletRequest request) {
        Map<String,String> thongBao = new HashMap<>();

        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            thongBao.put("message","Nhân viên chưa đăng nhập!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");
        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            thongBao.put("message","Sai định dạng hóa đơn!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        String cashPay = paymentData.get("cashPay");
        String cashAcountPay = paymentData.get("cashAcountPay");
        String cashBillPay = paymentData.get("cashBillPay");
        String notePay = paymentData.get("notePay");
        String payStatus = paymentData.get("payStatus");
        String surplusMoneyPay = paymentData.get("surplusMoneyPay");
        String payMethod = paymentData.get("payMethod");
        System.out.println("Du lieu khi thnah toan ");
        String billPay = paymentData.get("billPay");

        System.out.println("cashPay: " + cashPay);
        System.out.println("cashAcountPay: " + cashAcountPay);
        System.out.println("cashBillPay: " + cashBillPay);
        System.out.println("notePay: " + notePay);
        System.out.println("payStatus: " + payStatus);
        System.out.println("surplusMoneyPay: " + surplusMoneyPay);
        System.out.println("payMethod: " + payMethod);
        String validateCashPay = validateBigDecimal(cashPay);

        if(!validateCashPay.trim().equals("")) {
            thongBao.put("message","Sai định dạng tiền mặt!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        String validateCashAcountPay = validateBigDecimal(cashAcountPay);
        if(!validateCashAcountPay.trim().equals("")) {
            thongBao.put("message","Sai định dạng tiền tài khoản!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        String validateCashBillPay = validateBigDecimal(cashBillPay);
        if(!validateCashBillPay.trim().equals("")) {
            thongBao.put("message","Sai định dạng tiền hóa đơn!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        String validateSurplusMoneyPay = validateBigDecimal(surplusMoneyPay);
        if(!validateSurplusMoneyPay.trim().equals("")) {
            thongBao.put("message","Sai định dạng tiền trả lại!");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        String validatePayStatus = validateInteger(payStatus);
        if(!validatePayStatus.trim().equals("")) {
            thongBao.put("message","Sai định dạng trạng thái thanh toán");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        String validatePayMethod = validateInteger(payMethod);
        if(!validatePayMethod.trim().equals("")) {
            thongBao.put("message","Sai định dạng trangh thái thanh toán");
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }

        mess = "Thanh toan thanh cong";
        colorMess = "1";
        Integer checkPayMethod = Integer.parseInt(payMethod);
        if(checkPayMethod == 1) {
            Bill billPayment = this.billService.findById(idBill).orElse(null);
            if (billPayment == null) {
                thongBao.put("message","Hóa đơn không tồn tại!");
                thongBao.put("check","3");
                return ResponseEntity.ok(thongBao);
            }
            if(billPay.equals("billShip")) {

                if(billPayment.getStatus() >= 5 || billPayment.getPaymentStatus() == 1) {
                    thongBao.put("message","Hóa đơn này không thanh toán được!");
                    thongBao.put("check","3");
                    return ResponseEntity.ok(thongBao);
                }
                BigDecimal cash;
                try {
                    cash = new BigDecimal(cashPay).subtract(new BigDecimal(surplusMoneyPay));
                } catch (NumberFormatException e) {
                    thongBao.put("message","Số tiền thanh toán không hợp lệ!");
                    thongBao.put("check","3");
                    return ResponseEntity.ok(thongBao);
                }
                cash = new BigDecimal(cashPay).subtract(new BigDecimal(surplusMoneyPay));
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
                billPayment.setSurplusMoney(new BigDecimal(surplusMoneyPay));

                int result = billPayment.getCash().compareTo(billPayment.getTotalAmount().add(billPayment.getShippingPrice()).subtract(billPayment.getPriceDiscount()));

                if (result < 0) {
                    thongBao.put("message","Số tiền thanh toán không hợp lệ!");
                    thongBao.put("check","3");
                    return ResponseEntity.ok(thongBao);
                }

                this.billService.save(billPayment);
                thongBao.put("message",mess);
                thongBao.put("check",colorMess);
                this.setBillStatus(billPayment.getId(),101,session);
                return ResponseEntity.ok(thongBao);
            }else if (billPay.equals("exchangeBill")) {
                ReturnBillExchangeBill returnBillExchangeBill = this.returnBillService.getReturnBillByIdBill(billPayment.getId());

                if(returnBillExchangeBill == null) {
                    thongBao.put("message","Hóa đơn thanh toán đổi không tồn tại!");
                    thongBao.put("check","3");
                    return ResponseEntity.ok(thongBao);
                }

                if (returnBillExchangeBill.getId() == null) {
                    thongBao.put("message","Hóa đơn thanh toán đổi không tồn tại!");
                    thongBao.put("check","3");
                    return ResponseEntity.ok(thongBao);
                }

                String checkPayExchange = this.returnBillService.getStatusPaymentorNotPay(returnBillExchangeBill.getId());

                List<PaymentExchange> paymentExchanges = this.paymentExchangeService.getPaymentExchangeByIdBillExchange(returnBillExchangeBill.getId());

                if(checkPayExchange.equals("KhongPhaiTraTien")) {
                    thongBao.put("message","Hóa đơn này không thanh toán được!(exchange)");
                    thongBao.put("check","3");
                    return ResponseEntity.ok(thongBao);
                }

                if(!paymentExchanges.isEmpty()) {
                    thongBao.put("message","Hóa đơn này không thanh toán được!(exchange)");
                    thongBao.put("check","3");
                    return ResponseEntity.ok(thongBao);
                }

                BigDecimal cash;
                try {
                    cash = new BigDecimal(cashPay).subtract(new BigDecimal(surplusMoneyPay));
                } catch (NumberFormatException e) {
                    thongBao.put("message","Số tiền thanh toán không hợp lệ!");
                    thongBao.put("check","3");
                    return ResponseEntity.ok(thongBao);
                }
                cash = new BigDecimal(cashPay).subtract(new BigDecimal(surplusMoneyPay));

                if(notePay.trim().equals("")) {
                    session.setAttribute("notePayment","Thanh toán bằng tiền mặt!");
                }else {
                    session.setAttribute("notePayment",notePay);
                }

                PaymentExchange paymentExchange = new PaymentExchange();
                paymentExchange.setExchangeBilll(returnBillExchangeBill);
                paymentExchange.setCash(cash);
                paymentExchange.setCashAcount(new BigDecimal(0));
                paymentExchange.setPayMethod(1);
                paymentExchange.setStatus(1);
                paymentExchange.setStaff(staffLogin);
                paymentExchange.setSurplusMoney(new BigDecimal(surplusMoneyPay));

                System.out.println("cai nay cua exhcange thanh toan: " + paymentExchange.toString());
                thongBao.put("message","Thanh toán thành công(của đổi-trả sản phẩm)!");
                thongBao.put("check","1");
                thongBao.put("donePay","done");
                this.paymentExchangeService.save(paymentExchange);
                this.setBillStatus(billPayment.getId(),102,session);

                return ResponseEntity.ok(thongBao);
            }else {
                thongBao.put("message","Sai phương thức thanh toán!");
                thongBao.put("check","3");
                return ResponseEntity.ok(thongBao);
            }
        }else {
            Bill billPayment = this.billService.findById(idBill).orElse(null);

            if (billPay.equals("billShip")) {
                if (billPayment == null) {
                    thongBao.put("message","Hóa đơn không tồn tại!");
                    thongBao.put("check","3");
                    return ResponseEntity.ok(thongBao);
                }
                billPayment.setPaymentMethod(checkPayMethod);

                if(notePay.trim().equals("")) {
                    if(checkPayMethod == 2) {
                        notePay = "Thanh toán bằng tiền tài khoản!";
                        session.setAttribute("notePayment",notePay);
                    }else if (checkPayMethod == 3){
                        notePay = "Thanh toán bằng tiền tài khoan và tiền mặt!";
                        billPayment.setCash(BigDecimal.valueOf(Double.parseDouble(cashPay)));
                        session.setAttribute("notePayment",notePay);
                    }
                }else {
                    if(checkPayMethod == 2) {
                        session.setAttribute("notePayment",notePay);
                    }else if (checkPayMethod == 3){
                        billPayment.setCash(BigDecimal.valueOf(Double.parseDouble(cashPay)));
                        session.setAttribute("notePayment",notePay);
                    }
                }

                if(billPayment.getNote().trim().equals("")) {
                    billPayment.setNote(notePay);
                }
                session.setAttribute("billPaymentRest",billPayment);
                session.setAttribute("checkBill","billShip");
                String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                String vnpayUrl = vnPayService.createOrder((Integer.parseInt(cashAcountPay)), "chuyenKhoan", baseUrl);
                thongBao.put("vnpayUrl",vnpayUrl);
                return ResponseEntity.ok(thongBao);
            }else if (billPay.equals("exchangeBill")) {
                ReturnBillExchangeBill returnBillExchangeBill = this.returnBillService.getReturnBillByIdBill(billPayment.getId());

                if(returnBillExchangeBill == null) {
                    thongBao.put("message","Hóa đơn thanh toán đổi không tồn tại!");
                    thongBao.put("check","3");
                    return ResponseEntity.ok(thongBao);
                }

                if (returnBillExchangeBill.getId() == null) {
                    thongBao.put("message","Hóa đơn thanh toán đổi không tồn tại!");
                    thongBao.put("check","3");
                    return ResponseEntity.ok(thongBao);
                }

                String checkPayExchange = this.returnBillService.getStatusPaymentorNotPay(returnBillExchangeBill.getId());

                List<PaymentExchange> paymentExchanges = this.paymentExchangeService.getPaymentExchangeByIdBillExchange(returnBillExchangeBill.getId());

                PaymentExchange paymentExchange = new PaymentExchange();

                if(checkPayExchange.equals("KhongPhaiTraTien")) {
                    thongBao.put("message","Hóa đơn này không thanh toán được!(exchange)");
                    thongBao.put("check","3");
                    return ResponseEntity.ok(thongBao);
                }

                if(!paymentExchanges.isEmpty()) {
                    thongBao.put("message","Hóa đơn này không thanh toán được!(exchange)");
                    thongBao.put("check","3");
                    return ResponseEntity.ok(thongBao);
                }

                session.setAttribute("notePayment",notePay);

                if(!notePay.trim().equals("")) {
                    session.setAttribute("notePayment",notePay);
                }

                if(notePay.trim().equals("")) {
                    if(checkPayMethod == 2) {
                        notePay = "Thanh toán bằng tiền tài khoản(của đổi-trả sản phẩm)!";
                        session.setAttribute("notePayment",notePay);
                        paymentExchange.setPayMethod(2);
                    }else if (checkPayMethod == 3){
                        notePay = "Thanh toán bằng tiền tài khoan và tiền mặt(của đổi-trả sản phẩm)!";
                        billPayment.setCash(BigDecimal.valueOf(Double.parseDouble(cashPay)));
                        session.setAttribute("notePayment",notePay);
                        paymentExchange.setPayMethod(3);
                    }
                }else {
                    if(checkPayMethod == 2) {
                        session.setAttribute("notePayment",notePay);
                        paymentExchange.setPayMethod(2);
                    }else if (checkPayMethod == 3){
                        billPayment.setCash(BigDecimal.valueOf(Double.parseDouble(cashPay)));
                        session.setAttribute("notePayment",notePay);
                        paymentExchange.setPayMethod(3);
                    }
                }


                paymentExchange.setCash(new BigDecimal(cashPay));
                paymentExchange.setStatus(1);
                paymentExchange.setStaff(staffLogin);
                paymentExchange.setCashAcount(new BigDecimal(cashAcountPay));
                paymentExchange.setExchangeBilll(returnBillExchangeBill);
                paymentExchange.setSurplusMoney(new BigDecimal(0));

                System.out.println("exxchange tk: "+ paymentExchange.toString() );
//                thongBao.put("message","Hóa đơn này không thanh toán được!(exchange chuyen khoan)");
//                thongBao.put("check","3");
                session.setAttribute("exchangeBillPaymentRest",paymentExchange);
                session.setAttribute("checkBill","exchangeBill");
                String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                String vnpayUrl = vnPayService.createOrder((Integer.parseInt(cashAcountPay)), "chuyenKhoan", baseUrl);
                thongBao.put("vnpayUrl",vnpayUrl);
                return ResponseEntity.ok(thongBao);
            }else {
                thongBao.put("message","Sai phương thức thanh toán!");
                thongBao.put("check","3");
                return ResponseEntity.ok(thongBao);
            }
        }
    }
    @GetMapping("/infomation-payment-bill")
    public List<Object[]> getInfomationPaymentByIdBill(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");
        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            return null;
        }
        List<Object[]> results = billService.getInfoPaymentByIdBill((Integer) session.getAttribute("IdBill"));
        return results;
    }
    @GetMapping("/infomation-history-bill")
    public List<Object[]> getInfomationHistoryByIdBill(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }

        Integer idBill = (Integer) session.getAttribute("IdBill");
        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            return null;
        }
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

    @GetMapping("/bill-pdf/{idBill}")
    public ResponseEntity<byte[]> getbillPDF(@PathVariable("idBill") String idBill,HttpSession session) throws Exception {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        System.out.println("da vao phuong thuc xuat pdf");
        if(staffLogin == null) {
            System.out.println("pdf: neu khong dang nhap vao day");
            return null;
        }

        String validateIdBill = validateInteger(idBill);
        if (!validateIdBill.trim().equals("")) {
            System.out.println("pdf: neu id khong dung vao day");
            return null;
        }
        // Lấy chi tiết hóa đơn và thông tin hóa đơn từ service
        List<Object[]> billDetails = this.billService.getBillDetailByIdBillPDF(Integer.parseInt(idBill));
        List<Object[]> billInfoList = this.billService.getBillByIdCreatePDF(Integer.parseInt(idBill));

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

    @GetMapping("/bill-return-exchange-pdf/{idBill}")
    public ResponseEntity<byte[]> getBillReturnExchangePDF(@PathVariable("idBill") String idBill,HttpSession session) throws Exception {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        System.out.println("da vao phuong thuc xuat pdf");
        if(staffLogin == null) {
            System.out.println("pdf: neu khong dang nhap vao day");
            return null;
        }

        String validateIdBill = validateInteger(idBill);
        if (!validateIdBill.trim().equals("")) {
            System.out.println("pdf: neu id khong dung vao day");
            return null;
        }
        // Lấy chi tiết hóa đơn và thông tin hóa đơn từ service
        List<Object[]> exchangeDetailList = this.billService.getListProductExchange(Integer.parseInt(idBill));
        List<Object[]> returnDetailList = this.billService.getListProductReturn(Integer.parseInt(idBill));
        List<Object[]> billInfoList = this.billService.getInformationPDF_Return_Exchange_Bill(Integer.parseInt(idBill));

        // Kiểm tra dữ liệu
        if (billInfoList == null || returnDetailList.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        Object[] billInfo = billInfoList.get(0);

        // Tạo PDF từ template
        ByteArrayOutputStream pdfStream = pdfTemplateService.fillPdfReturnExchangeTemplate(billInfo,returnDetailList,exchangeDetailList);
        byte[] pdfBytes = pdfStream.toByteArray();

        // Đường dẫn thư mục và file
        String directoryPath = "D:/danhSachHoaDonReturnExchange/";
        String filePath = directoryPath + billInfo[2] + ".pdf";

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
        headers.setContentDispositionFormData("inline", billInfo[2] + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/check-payment-exchange")
    public String getCheckPaymentExchange(HttpSession session) {
        Integer idBill = (Integer) session.getAttribute("IdBill");
        String validateIdBill = validateInteger(idBill != null ? idBill.toString() : "");
        if (!validateIdBill.trim().equals("")) {
            return null;
        }

        Bill billPayment = this.billService.findById(idBill).orElse(null);
        if (billPayment == null) {
            return null;
        }

        ReturnBillExchangeBill returnBillExchangeBill = this.returnBillService.getReturnBillByIdBill(billPayment.getId());

        if(returnBillExchangeBill == null) {
            return null;
        }

        if (returnBillExchangeBill.getId() == null) {
            return null;
        }

        String checkPayExchange = this.returnBillService.getStatusPaymentorNotPay(returnBillExchangeBill.getId());

        if(checkPayExchange == null) {
            return "notBtnPayExchange";
        }

        System.out.println("check exchange " + checkPayExchange);
        List<PaymentExchange> paymentExchanges = this.paymentExchangeService.getPaymentExchangeByIdBillExchange(returnBillExchangeBill.getId());

        if(checkPayExchange.equals("PhaiTraTien") && paymentExchanges.isEmpty()) {
            return "btnPayExchange";
        }

        return "notBtnPayExchange";
    }


}