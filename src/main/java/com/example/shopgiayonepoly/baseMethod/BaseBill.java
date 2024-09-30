package com.example.shopgiayonepoly.baseMethod;

import com.example.shopgiayonepoly.dto.request.bill.ProductDetailCheckRequest;
import com.example.shopgiayonepoly.dto.request.VoucherRequest;
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
import java.util.Date;
import java.util.List;

public abstract class BaseBill {
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
    protected InvoicePdfService invoicePdfService;


    //bien cuc bo cua bill
    protected Bill billPay;
    protected String mess = "";
    protected String colorMess = "";
    protected Integer pageProduct = 0;
    protected String keyVoucher = "";
    protected Integer statusBillCheck = null;
    protected String keyBillmanage = "";
    protected ProductDetailCheckRequest productDetailCheckRequest;


    //method chung
    //sua lai gia khi them san pham
    protected void setTotalAmount(Bill bill) {
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

    //    sua lai so luong san pham khi duoc dua vao hoa don
    protected void getUpdateQuantityProduct(Integer idProductDetail,Integer quantity,Integer status) {
//        ProductDetail productDetail = this.billDetailService.getProductDetailById(idProductDetail);

        if (status == 1) {
//            productDetail.setQuantity(productDetail.getQuantity() - quantity);
            System.out.println("da tru");
        } else {
//            productDetail.setQuantity(productDetail.getQuantity() + quantity);
            System.out.println("da cong");
        }

//        productDetail.setUpdateDate(new Date());
    }
//danh cho giao hang, cai nay dung de theo doi don hang
protected void setBillStatus(Integer idBillSet,Integer status,HttpSession session) {
    Bill bill = this.billService.findById(idBillSet).orElse(null);
    Staff staff = (Staff) session.getAttribute("staffLogin");
    InvoiceStatus invoiceStatus = new InvoiceStatus();
    invoiceStatus.setBill(bill);
    invoiceStatus.setStatus(status);
    if (invoiceStatus.getStatus() == 0) {
        session.setAttribute("notePayment","Tạo Đơn Hàng!");
    }else if (invoiceStatus.getStatus() == 1) {
        session.setAttribute("notePayment","Chờ Xác nhận!");
    }else if(invoiceStatus.getStatus() == 2) {
        session.setAttribute("notePayment","Đã xác nhận!");
    }else if (invoiceStatus.getStatus() == 3) {
        session.setAttribute("notePayment","Giao Hàng!");
    }else if (invoiceStatus.getStatus() == 4) {
        session.setAttribute("notePayment","Khách đã nhận được hàng!");
    }else if (invoiceStatus.getStatus() == 5) {
        session.setAttribute("notePayment","Đơn Hàng Đã Hoàn Thành!");
    }else if (invoiceStatus.getStatus() == 6){
        session.setAttribute("notePayment","Đơn Hàng Đã Bị Hủy!");
    }else if (invoiceStatus.getStatus() == 101) {
        session.setAttribute("notePayment","Đơn Hàng Đã được thanh toán!");
    }
    invoiceStatus.setNote(staff.getId()+","+session.getAttribute("notePayment"));
    session.removeAttribute("notePayment");
    this.invoiceStatusService.save(invoiceStatus);
}
    //trừ đi voucher cua hóa đơn
    protected void getSubtractVoucher(Voucher voucher) {
        voucher.setQuantity(voucher.getQuantity() - 1);
        voucher.setUpdateDate(new Date());
        VoucherRequest voucherRequest = new VoucherRequest();
        BeanUtils.copyProperties(voucher,voucherRequest);
        System.out.println(voucherRequest.toString());
        System.out.println(voucherRequest.getId() + "" + voucherRequest.getCreateDate() + "" + voucherRequest.getUpdateDate());
        System.out.println(voucher.getId() + "" + voucher.getCreateDate() + "" + voucher.getUpdateDate());
        this.voucherService.updateVoucher(voucherRequest);
    }

    //    Phân trang cho product
    protected Page<ProductDetail> convertListToPage(List<ProductDetail> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<ProductDetail> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }

    //danh cho khi them san pham vao bill
    protected BillDetail getBuyProduct(Bill idBill,ProductDetail idProductDetail,Integer quantity) {
        BillDetail billDetail;

        Integer idBillDetail = this.billDetailService.getBillDetailExist(idBill.getId(),idProductDetail.getId());
        if(idBillDetail != null) {
            billDetail = this.billDetailService.findById(idBillDetail).orElse(new BillDetail());
            billDetail.setQuantity(billDetail.getQuantity()+quantity);
            billDetail.setTotalAmount(billDetail.getPrice().multiply(BigDecimal.valueOf(billDetail.getQuantity())));
        }else {
            billDetail = new BillDetail();
            billDetail.setBill(idBill);
            billDetail.setProductDetail(idProductDetail);
            billDetail.setQuantity(quantity);
            BigDecimal priceBuy;
            if (idProductDetail.getSaleProduct() != null) {
                if (idProductDetail.getSaleProduct().getDiscountType() == 1) {
                    // Tính phần trăm giảm (discountValue là %)
                    BigDecimal percent = idProductDetail.getSaleProduct().getDiscountValue().divide(new BigDecimal("100"));

                    // Tính số tiền giảm: Lấy giá gốc nhân với phần trăm giảm
                    BigDecimal pricePercent = idProductDetail.getPrice().multiply(percent);

                    // Tính giá sau khi giảm
                    priceBuy = idProductDetail.getPrice().subtract(pricePercent);
                } else {
                    // Trường hợp giảm trực tiếp theo giá trị cụ thể
                    priceBuy = idProductDetail.getPrice().subtract(idProductDetail.getSaleProduct().getDiscountValue());
                }
                billDetail.setPrice(priceBuy);
            } else {
                // Không có giảm giá
                priceBuy = idProductDetail.getPrice();
                billDetail.setPrice(priceBuy);
            }

            billDetail.setPriceRoot(idProductDetail.getPrice());
            billDetail.setTotalAmount(billDetail.getPrice().multiply(BigDecimal.valueOf(billDetail.getQuantity())));
            billDetail.setStatus(1);
        }
        return  billDetail;
    }

    //quet san pham co thay doi gia khong
    protected void displayProductDetailsWithCurrentPrice() {
        this.productDetailCheckRequest = new ProductDetailCheckRequest("", null, null, null, null, null, null);
        // Lấy tất cả ProductDetail từ repository
        List<ProductDetail> productDetails = this.billDetailService.getProductDetailSale(this.productDetailCheckRequest);
        // Lặp qua từng ProductDetail để tính toán giá hiện tại
        for (ProductDetail productDetail : productDetails) {
            BigDecimal currentPrice = productDetail.getPrice(); // Giá gốc
            // Kiểm tra xem có giảm giá hay không
            if (productDetail.getSaleProduct() != null) {
                SaleProduct saleProduct = productDetail.getSaleProduct();
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
            // Lấy tất cả BillDetail từ repository
            List<BillDetail> billDetails = this.billDetailService.findAll();
            // Lặp qua các BillDetail và lọc những bill có status == 0
            for (BillDetail billDetail : billDetails) {
                Bill bill = billDetail.getBill();
                // Kiểm tra nếu status của bill == 0
                if (bill.getPaymentStatus() == 0) {
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
                            billDetailSave.setUpdateDate(new Date());
                            billDetailSave.setPrice(currentPrice);
                            billDetailSave.setTotalAmount(billDetailSave.getPrice().multiply(BigDecimal.valueOf(billDetailSave.getQuantity())));
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

}
