package com.example.shopgiayonepoly.baseMethod;

import com.example.shopgiayonepoly.dto.request.Shift.CashierInventoryFilterByIdStaffRequest;
import com.example.shopgiayonepoly.dto.request.bill.ProductDetailCheckMark2Request;
import com.example.shopgiayonepoly.dto.request.bill.ProductDetailCheckRequest;
import com.example.shopgiayonepoly.dto.request.VoucherRequest;
import com.example.shopgiayonepoly.dto.request.bill.SearchBillByStatusRequest;
import com.example.shopgiayonepoly.dto.response.bill.ExchangeBillDetailResponse;
import com.example.shopgiayonepoly.dto.response.bill.ReturnBillDetailResponse;
import com.example.shopgiayonepoly.entites.*;
import com.example.shopgiayonepoly.service.*;
import com.example.shopgiayonepoly.service.attribute.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public abstract class BaseBill extends BaseEmail {
    @Autowired
    protected BillService billService;
    @Autowired
    protected BillDetailService billDetailService;
    @Autowired
    protected VNPayService vnPayService;
    @Autowired
    protected VoucherService voucherService;
    @Autowired
    protected InvoiceStatusService invoiceStatusService;
    //Atribute filter
    @Autowired
    protected ColorService colorService;
    @Autowired
    protected SoleService soleService;
    @Autowired
    protected SizeService sizeService;
    @Autowired
    protected MaterialService materialService;
    @Autowired
    protected ManufacturerService manufacturerService;
    @Autowired
    protected OriginService originService;
    @Autowired
    protected HistoryService historyService;
    @Autowired
    protected PdfTemplateService pdfTemplateService;
    @Autowired
    protected ProductDetailService productDetailService;
    @Autowired
    protected ReturnBillService returnBillService;
    @Autowired
    protected ReturnBillDetailService returnBillDetailService;
    @Autowired
    protected SaleProductService saleProductService;
    @Autowired
    protected ExchangeBillDetailService exchangeBillDetailService;
    @Autowired
    protected CustomerService customerService;
    @Autowired
    protected EmailSenderService emailSenderService;
    @Autowired
    protected PaymentExchangeService paymentExchangeService;
    @Autowired
    protected TransactionVNPayService transactionVNPayService;
    @Autowired
    protected TimekeepingService timekeepingService;
    @Autowired
    protected CashierInventoryService cashierInventoryService;
    @Autowired
    protected ClientService clientService;

    //bien cuc bo cua bill
    protected Bill billPay;
    protected String mess = "";
    protected String colorMess = "";
    protected Integer pageProduct = 0;
    protected String keyVoucher = "";
    //    protected Integer statusBillCheck = null;
    protected Integer[] statusBillCheck = null;
    protected SearchBillByStatusRequest searchBillByStatusRequest;
    protected String keyBillmanage = "";
    protected Date keyStartDate;
    protected Date keyEndDate;
    protected ProductDetailCheckRequest productDetailCheckRequest;
    protected ProductDetailCheckMark2Request productDetailCheckMark2Request;

    // danh cho tra hang
    protected List<ReturnBillDetailResponse> returnBillDetailResponses = new ArrayList<>();
    protected Integer quantity = 0;
    protected Integer idProductDetail = 0;
    protected BigDecimal totalReturn = BigDecimal.valueOf(0);
    protected List<BillDetail> billDetailList;

    //danh cho doi hang
    protected List<ExchangeBillDetailResponse> exchangeBillDetailResponses = new ArrayList<>();

    protected BigDecimal totalExchange = BigDecimal.valueOf(0);

    protected BigDecimal exchangeAndReturnFee = BigDecimal.valueOf(0);
    protected BigDecimal discountedAmount = BigDecimal.valueOf(0);

    protected CashierInventoryFilterByIdStaffRequest cashierInventoryFilterByIdStaffRequest = null;

    //method chung
    //sua lai gia khi them san pham
    protected void setTotalAmount(Bill bill) {
        BigDecimal total = this.billDetailService.getTotalAmountByIdBill(bill.getId());
        if (total != null) {
            bill.setUpdateDate(new Date());
            bill.setTotalAmount(total);
        } else {
            bill.setUpdateDate(new Date());
            bill.setTotalAmount(BigDecimal.valueOf(0));
        }
        System.out.println("Thong tin bill: " + bill.toString());
        Bill billSave = this.billService.save(bill);
        if (billSave.getStatus() == 1) {
            System.out.println("gia duoc giam been xoa " + this.billService.getDiscountBill(billSave.getId()));
            billSave.setPriceDiscount(new BigDecimal(this.billService.getDiscountBill(billSave.getId())));
            this.billService.save(billSave);
        }
    }

    //    sua lai so luong san pham khi duoc dua vao hoa don
    protected void getUpdateQuantityProduct(Integer idProductDetial, Integer quantity) {
        ProductDetail detail = this.billService.getProductDteailById(idProductDetial);
        detail.setUpdateDate(new Date());
        Integer quantityNew = detail.getQuantity() - quantity;
        if (quantityNew <= 0) {
            detail.setQuantity(0);
        } else {
            detail.setQuantity(quantityNew);
        }
        System.out.println("da update so luong san pham");
        this.productDetailService.save(detail);
    }
    //update

    //danh cho giao hang, cai nay dung de theo doi don hang
    protected void setBillStatus(Integer idBillSet, Integer status, HttpSession session) {
        Bill bill = this.billService.findById(idBillSet).orElse(null);
        Staff staff = (Staff) session.getAttribute("staffLogin");
        InvoiceStatus invoiceStatus = new InvoiceStatus();
        invoiceStatus.setBill(bill);
        invoiceStatus.setStatus(status);
        String checkNote = (String) session.getAttribute("notePayment");

        if (invoiceStatus.getStatus() == 0) {
            if (checkNote == null || checkNote.trim().isEmpty()) {
                session.setAttribute("notePayment", "Tạo Đơn Hàng!");
            }
        } else if (invoiceStatus.getStatus() == 1) {
            if (checkNote == null || checkNote.trim().isEmpty()) {
                session.setAttribute("notePayment", "Chờ Xác nhận!");
            }
        } else if (invoiceStatus.getStatus() == 2) {
            mess = "Hóa đơn đã được xác nhận!";
            colorMess = "1";
            if (checkNote == null || checkNote.trim().isEmpty()) {
                session.setAttribute("notePayment", "Đã xác nhận!");
            }
        } else if (invoiceStatus.getStatus() == 3) {
            mess = "Hóa đơn đã được giao hàng!";
            colorMess = "1";
            if (checkNote == null || checkNote.trim().isEmpty()) {
                session.setAttribute("notePayment", "Giao Hàng!");
            }
        } else if (invoiceStatus.getStatus() == 4) {
            mess = "Hóa đơn đã được khách nhận!";
            colorMess = "1";
            if (checkNote == null || checkNote.trim().isEmpty()) {
                session.setAttribute("notePayment", "Khách đã nhận được hàng!");
            }
        } else if (invoiceStatus.getStatus() == 5) {
            mess = "Hóa đơn đã hoàn thành!";
            colorMess = "1";
            if (checkNote == null || checkNote.trim().isEmpty()) {
                session.setAttribute("notePayment", "Đơn Hàng Đã Hoàn Thành!");
            }
        } else if (invoiceStatus.getStatus() == 6) {
            if (checkNote == null || checkNote.trim().isEmpty()) {
                session.setAttribute("notePayment", "Đơn Hàng Đã Bị Hủy!");
            }
        } else if (invoiceStatus.getStatus() == 101) {
            if (checkNote == null || checkNote.trim().isEmpty()) {
                session.setAttribute("notePayment", "Đơn Hàng Đã được thanh toán!");
            }
        } else if (invoiceStatus.getStatus() == 102) {
            if (checkNote == null || checkNote.trim().isEmpty()) {
                session.setAttribute("notePayment", "Đơn Hàng Đã được thanh toán!");
            }
        } else if (invoiceStatus.getStatus() == 201) {
            if (checkNote == null || checkNote.trim().isEmpty()) {
                session.setAttribute("notePayment", "Chờ xác nhận đổi-trả hàng!");
            }
        } else if (invoiceStatus.getStatus() == 202) {
            if (checkNote == null || checkNote.trim().isEmpty()) {
                session.setAttribute("notePayment", "Đồng ý đổi-trả hàng!");
            }
            ReturnBillExchangeBill returnBill = this.returnBillService.getReturnBillByIdBill(bill.getId());
            returnBill.setUpdateDate(new Date());
            returnBill.setStatus(1);
            this.returnBillService.save(returnBill);
        } else if (invoiceStatus.getStatus() == 203) {
            if (checkNote == null || checkNote.trim().isEmpty()) {
                session.setAttribute("notePayment", "Không đồng ý đổi-trả hàng!");
            }
            ReturnBillExchangeBill returnBill = this.returnBillService.getReturnBillByIdBill(bill.getId());
            returnBill.setUpdateDate(new Date());
            returnBill.setStatus(2);
            this.returnBillService.save(returnBill);
        }
        if (staff == null) {
            invoiceStatus.setNote("Không có" + "," + session.getAttribute("notePayment"));
        } else {
            invoiceStatus.setNote(staff.getId() + "," + session.getAttribute("notePayment"));
        }
        session.removeAttribute("notePayment");
        this.invoiceStatusService.save(invoiceStatus);
    }

    //trừ đi voucher cua hóa đơn
    protected void getSubtractVoucher(Voucher voucher, Integer quantity) {
        voucher.setQuantity(voucher.getQuantity() - quantity);
        voucher.setUpdateDate(new Date());
        VoucherRequest voucherRequest = new VoucherRequest();
        BeanUtils.copyProperties(voucher, voucherRequest);
        System.out.println(voucherRequest.toString());
        System.out.println(voucherRequest.getId() + "" + voucherRequest.getCreateDate() + "" + voucherRequest.getUpdateDate());
        System.out.println(voucher.getId() + "" + voucher.getCreateDate() + "" + voucher.getUpdateDate());
        this.voucherService.updateVoucher(voucherRequest);
    }

    //    Phân trang cho product
    protected Page<Object[]> convertListToPage(List<Object[]> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<Object[]> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }

    //danh cho khi them san pham vao bill
    protected BillDetail getBuyProduct(Bill idBill, ProductDetail idProductDetail, Integer quantity) {
        BillDetail billDetail;
        Integer idBillDetail = this.billDetailService.getBillDetailExist(idBill.getId(), idProductDetail.getId());
        BigDecimal priceCheck;

        // Trường hợp tồn tại BillDetail
        if (idBillDetail != -1) {
            billDetail = this.billDetailService.findById(idBillDetail).orElse(new BillDetail());

            // Kiểm tra nếu sản phẩm có giảm giá
            if (idProductDetail.getSaleProduct() != null) {
                priceCheck = getPriceAfterDiscount(idProductDetail);
            } else {
                priceCheck = billDetail.getPrice() != null ? billDetail.getPrice() : BigDecimal.ZERO;
            }

            // Kiểm tra sự thay đổi của giá
            if (priceCheck.compareTo(billDetail.getPrice() != null ? billDetail.getPrice() : BigDecimal.ZERO) != 0) {
                billDetail = new BillDetail();
                billDetail.setBill(idBill);
                billDetail.setProductDetail(idProductDetail);
                billDetail.setQuantity(quantity);
                billDetail.setPrice(priceCheck);
                billDetail.setPriceRoot(idProductDetail.getPrice() != null ? idProductDetail.getPrice() : BigDecimal.ZERO);
                billDetail.setTotalAmount(priceCheck.multiply(BigDecimal.valueOf(quantity)));
                billDetail.setStatus(1);
                return billDetail;
            }

            // Cập nhật số lượng và tổng tiền nếu giá không thay đổi
            billDetail.setQuantity(billDetail.getQuantity() + quantity);
            billDetail.setTotalAmount(billDetail.getPrice().multiply(BigDecimal.valueOf(billDetail.getQuantity())));
        } else {
            // Trường hợp không tồn tại BillDetail
            billDetail = new BillDetail();
            billDetail.setBill(idBill);
            billDetail.setProductDetail(idProductDetail);
            billDetail.setQuantity(quantity);

            BigDecimal priceBuy = idProductDetail.getSaleProduct() != null
                    ? getPriceAfterDiscount(idProductDetail)
                    : idProductDetail.getPrice() != null ? idProductDetail.getPrice() : BigDecimal.ZERO;

            billDetail.setPrice(priceBuy);
            billDetail.setPriceRoot(idProductDetail.getPrice() != null ? idProductDetail.getPrice() : BigDecimal.ZERO);
            billDetail.setTotalAmount(priceBuy.multiply(BigDecimal.valueOf(quantity)));
            billDetail.setStatus(1);
        }
        return billDetail;
    }


    //quet san pham co thay doi gia khong
    protected void displayProductDetailsWithCurrentPrice() {
        this.productDetailCheckRequest = new ProductDetailCheckRequest("", null, null, null, null, null, null);
        // Lấy tất cả ProductDetail từ repository
        List<ProductDetail> productDetails = this.billDetailService.getProductDetailSale(this.productDetailCheckRequest);
        // Lặp qua từng ProductDetail để tính toán giá hiện tại
        for (ProductDetail productDetail : productDetails) {
            BigDecimal currentPrice = productDetail.getPrice(); // Giá gốc
            // Kiểm tra xem có giảm giá và đợt giảm giá còn trong thời gian áp dụng hay không
            if (productDetail.getSaleProduct() != null) {
                SaleProduct saleProduct = productDetail.getSaleProduct();
                LocalDate now = LocalDate.now(); // Thời gian hiện tại dưới dạng LocalDate

                // Giả sử startDate và endDate là kiểu LocalDate, không cần chuyển đổi
                LocalDate startDate = saleProduct.getStartDate();
                LocalDate endDate = saleProduct.getEndDate();

                // Kiểm tra ngày bắt đầu và kết thúc giảm giá
                if ((startDate == null || !now.isBefore(startDate)) &&
                        (endDate == null || !now.isAfter(endDate)) && saleProduct.getStatus() == 1) {

                    // Kiểm tra loại giảm giá (1 = giảm theo %, 2 = giảm theo giá trị cụ thể)
                    if (saleProduct.getDiscountType() == 1) {
                        // Tính phần trăm giảm
                        BigDecimal percent = saleProduct.getDiscountValue().divide(new BigDecimal("100"));
                        BigDecimal discountAmount = productDetail.getPrice().multiply(percent);
                        currentPrice = productDetail.getPrice().subtract(discountAmount); // Tính giá sau giảm
                    } else if (saleProduct.getDiscountType() == 2) {
                        // Giảm theo giá trị cụ thể
                        currentPrice = productDetail.getPrice().subtract(saleProduct.getDiscountValue());
                    }
                }
            }

            // Lấy tất cả BillDetail từ repository
            List<BillDetail> billDetails = this.billDetailService.findAll();
            // Lặp qua các BillDetail và lọc những bill có status == 0
            for (BillDetail billDetail : billDetails) {
                Bill bill = billDetail.getBill();
                // Kiểm tra nếu status của bill == 0
                if (bill.getStatus() == 0) {
                    // So sánh ID của ProductDetail và BillDetail để kiểm tra tính hợp lệ
                    if (billDetail.getProductDetail().getId().equals(productDetail.getId())) {
                        // Sử dụng compareTo() để so sánh giá
                        if (billDetail.getPrice().compareTo(currentPrice) == 0) {
                            System.out.println("Bill ID: " + bill.getId() + ", BillDetail ID: " + billDetail.getId() +
                                    ", Product ID: " + billDetail.getProductDetail().getId() +
                                    ", Quantity: " + billDetail.getQuantity() +
                                    ", Price(trong bill): " + billDetail.getPrice() +
                                    ", Price(trong san pham): " + currentPrice +
                                    ", ỔN");
                        } else {
                            System.out.println("Bill ID: " + bill.getId() + ", BillDetail ID: " + billDetail.getId() +
                                    ", Product ID: " + billDetail.getProductDetail().getId() +
                                    ", Quantity: " + billDetail.getQuantity() +
                                    ", Price(trong bill): " + billDetail.getPrice() +
                                    ", Price(trong san pham): " + currentPrice +
                                    ", ko ỔN");
                            BillDetail billDetailSave = billDetail;
                            billDetailSave.setPriceRoot(productDetail.getPrice());
                            billDetailSave.setUpdateDate(new Date());
                            billDetailSave.setPrice(currentPrice);
                            billDetailSave.setTotalAmount(billDetailSave.getPrice().multiply(BigDecimal.valueOf(billDetailSave.getQuantity())));
                            if (billDetailSave.getPrice().compareTo(new BigDecimal(0)) < 0) {
                                billDetailSave.setPrice(new BigDecimal(0));
                                billDetailSave.setTotalAmount(billDetailSave.getPrice().multiply(BigDecimal.valueOf(billDetailSave.getQuantity())));
                            }
                            this.billDetailService.save(billDetailSave);
                            setTotalAmount(billDetail.getBill());
                        }
                    }
                }
            }
        }
    }


    //them vao lich su neu co su thay doi
    protected void getAddHistory(
            String nameTable,
            Integer idColum,
            String nameAtribute,
            String newValue,
            String oldValue,
            Staff staff,
            String note
    ) {
        History history = new History();
        history.setAtTime(new Date());
        history.setNameTable(nameTable);
        history.setIdTable(idColum);
        history.setAttributeName(nameAtribute);
        history.setNewValue(newValue);
        history.setOldValue(oldValue);
        history.setStaff(staff);
        history.setNote(note);
        this.historyService.save(history);
    }

    //xoa voucher khi giap ap dung khong hop ly
    protected void getDeleteVoucherByBill(Integer idBill) {
        System.out.println("da vao day de quet voucher");
        if (idBill != null) {
            Bill bill = this.billService.findById(idBill).orElse(null);
            System.out.println("day ne " + bill.toString());
            if (bill.getVoucher() != null) {
                Voucher voucher = this.voucherService.getOne(bill.getVoucher().getId());
                if (bill.getTotalAmount().compareTo(voucher.getPricesApply()) < 0) {
                    System.out.println("phai xoa thoii");
                    this.getSubtractVoucher(bill.getVoucher(), -1);
                    bill.setVoucher(null);
                    bill.setPriceDiscount(new BigDecimal(0));
                    bill.setUpdateDate(new Date());
                    this.billService.save(bill);
                }
            }
        } else {
            System.out.println("hoa don nay co");
        }
    }

    //them gia tien duoc giam vao db
    protected void getInsertPriceDiscount(Integer idBill) {
        Bill bill = this.billService.findById(idBill).orElse(null);
        if (bill != null) {
            BigDecimal discount = BigDecimal.valueOf(0); // Khởi tạo giá trị giảm mặc định là 0
            // Kiểm tra xem có voucher không
            if (bill.getVoucher() != null) {
                Voucher voucher = bill.getVoucher();
                // Kiểm tra loại giảm giá (1 là theo %)
                if (voucher.getDiscountType() == 1) {
                    // Giảm giá theo phần trăm
                    BigDecimal discountPercentage = voucher.getPriceReduced(); // % giảm giá
                    BigDecimal discountAmount = bill.getTotalAmount().multiply(discountPercentage).divide(BigDecimal.valueOf(100));
                    // Nếu giá trị giảm lớn hơn giá trị tối đa được phép giảm
                    if (discountAmount.compareTo(voucher.getPricesMax()) > 0) {
                        discount = voucher.getPricesMax(); // Giảm giá tối đa bằng pricesMax
                    } else {
                        discount = discountAmount; // Áp dụng giá trị giảm theo %
                    }
                } else {
                    // Giảm giá theo số tiền cụ thể
                    discount = voucher.getPriceReduced(); // Giảm trực tiếp theo số tiền đã định
                }
            }
            // Cập nhật giá trị giảm giá vào hóa đơn
            bill.setPriceDiscount(discount);
            // Lưu hóa đơn
            this.billService.save(bill);
        }
    }

    //validate Bigdecimal
    protected String validateBigDecimal(String val) {
        try {
            if (val.trim().equals("") || val == null) {
                return "/404";
            }
            new BigDecimal(val);
            return "";
        } catch (NumberFormatException e) {
            return "/404";
        }
    }

    //validate Integer
    protected String validateInteger(String val) {
        try {
            if (val.trim().equals("") || val == null) {
                return "/404";
            }
            Integer.parseInt(val);
            return "";
        } catch (NumberFormatException e) {
            return "/404";
        }
    }

    //so sanh gia
    protected BigDecimal getPriceAfterDiscount(ProductDetail productDetail) {
        BigDecimal priceBuy = productDetail.getPrice();

        // Kiểm tra nếu SaleProduct không phải null
        if (productDetail.getSaleProduct() != null) {
            if(productDetail.getSaleProduct().getStatus() == 1) {
                LocalDate startDate = productDetail.getSaleProduct().getStartDate();
                LocalDate endDate = productDetail.getSaleProduct().getEndDate();
                LocalDate today = LocalDate.now();

                // Kiểm tra ngày hiện tại có nằm trong khoảng startDate và endDate hay không
                if (startDate != null && endDate != null && (today.isEqual(startDate) || today.isEqual(endDate) || (today.isAfter(startDate) && today.isBefore(endDate)))) {
                    BigDecimal discountValue = productDetail.getSaleProduct().getDiscountValue();

                    if (productDetail.getSaleProduct().getDiscountType() == 1) {
                        // Tính phần trăm giảm (discountValue là %)
                        BigDecimal percent = discountValue.divide(BigDecimal.valueOf(100));
                        BigDecimal pricePercent = productDetail.getPrice().multiply(percent);
                        priceBuy = productDetail.getPrice().subtract(pricePercent);
                    } else {
                        // Trường hợp giảm trực tiếp theo giá trị cụ thể
                        priceBuy = productDetail.getPrice().subtract(discountValue);
                    }
                    // Đảm bảo giá sau khi giảm không âm
                    return priceBuy.max(BigDecimal.ZERO);
                }
            }
        }

        // Nếu không có khuyến mãi hoặc khuyến mãi không còn hiệu lực, trả về giá gốc
        return productDetail.getPrice();
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

    protected Map<String,String> checkLoginAndLogOutByStaff(Integer idStaff) {
        Map<String,String> thongBao = new HashMap<>();
        String checkLogin = getCheckStaffAttendanceYetBill(idStaff,1);
        String checkLogOut = getCheckStaffAttendanceYetBill(idStaff,2);
        System.out.println(checkLogin);
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

    protected String getCheckStaffCashierInventory(
            Integer idStaff
    ) {
        List<Object[]> checkCashierInventory = this.cashierInventoryService.getCheckCashierInventoryStaff(idStaff);

        // Kiểm tra nếu danh sách không rỗng và có kết quả
        if (!checkCashierInventory.isEmpty() && checkCashierInventory.get(0).length > 0) {
            // Lấy giá trị đầu tiên từ kết quả
            return checkCashierInventory.get(0)[0].toString();
        }
        // Trường hợp không có dữ liệu
        return "Không";
    }

}
