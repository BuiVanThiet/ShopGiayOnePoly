package com.example.shopgiayonepoly.baseMethod;

import com.example.shopgiayonepoly.dto.request.ProductDetailCheckRequest;
import com.example.shopgiayonepoly.dto.request.VoucherRequest;
import com.example.shopgiayonepoly.entites.*;
import com.example.shopgiayonepoly.service.*;
import com.example.shopgiayonepoly.service.attribute.*;
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
        ProductDetail productDetail = this.billDetailService.getProductDetailById(idProductDetail);

        if (status == 1) {
            productDetail.setQuantity(productDetail.getQuantity() - quantity);
            System.out.println("da tru");
        } else {
            productDetail.setQuantity(productDetail.getQuantity() + quantity);
            System.out.println("da cong");
        }

        productDetail.setUpdateDate(new Date());
    }
//danh cho giao hang, cai nay dung de theo doi don hang
protected void setBillStatus(Integer idBillSet) {
    Bill bill = this.billService.findById(idBillSet).orElse(null);
    InvoiceStatus status = new InvoiceStatus();
    status.setBill(bill);
    status.setStatus(bill.getStatus());
    this.invoiceStatusService.save(status);
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
            billDetail.setPrice(idProductDetail.getPrice());
            billDetail.setTotalAmount(billDetail.getPrice().multiply(BigDecimal.valueOf(billDetail.getQuantity())));
            billDetail.setStatus(1);
        }
        return  billDetail;
    }
}
