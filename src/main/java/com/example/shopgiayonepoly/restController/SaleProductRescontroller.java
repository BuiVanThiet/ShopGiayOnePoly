package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.dto.request.SaleProductRequest;
import com.example.shopgiayonepoly.dto.request.VoucherRequest;
import com.example.shopgiayonepoly.dto.request.Voucher_SaleProductSearchRequest;
import com.example.shopgiayonepoly.dto.request.bill.ProductDetailCheckMark2Request;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.SaleProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api-sale-product")
public class SaleProductRescontroller {
    @Autowired
    SaleProductService saleProductService;
    Voucher_SaleProductSearchRequest saleProductSearchRequest = null;

    ProductDetailCheckMark2Request productDetailCheckMark2Request = null;
    Integer checkIdSaleProduct = null;
    //tai danh sach
    @GetMapping("/list/{page}")
    public List<Object[]> getListSaleProduct(@PathVariable("page") String page) {
        if(saleProductSearchRequest == null) {
            saleProductSearchRequest = new Voucher_SaleProductSearchRequest(null,"",1);
        }
        System.out.println("du lieu loc: " + saleProductSearchRequest.toString());
        List<Object[]> getList = this.saleProductService.getAllSaleProductByFilter(saleProductSearchRequest.getDiscountTypeCheck(),saleProductSearchRequest.getNameCheck(),saleProductSearchRequest.getStatusCheck());
        System.out.println("benvoucher resst");
        for (Object[] Object: getList) {
            System.out.println(Object[1]);
        }
        System.out.println("het");
        Pageable pageable = PageRequest.of(Integer.parseInt(page)-1,5);
        return convertListToPage(getList,pageable).getContent();
    }

    //phan trang
    @GetMapping("/max-page-sale-product")
    public Integer getMaxPageSaleProduct() {
        if(saleProductSearchRequest == null) {
            saleProductSearchRequest = new Voucher_SaleProductSearchRequest(null,"",1);
        }
        List<Object[]> getList = this.saleProductService.getAllSaleProductByFilter(saleProductSearchRequest.getDiscountTypeCheck(),saleProductSearchRequest.getNameCheck(),saleProductSearchRequest.getStatusCheck());
        Integer pageNumber = (int) Math.ceil((double) getList.size() / 5);
        return pageNumber;
    }

    //bo loc
    @PostMapping("/search-sale-product")
    public Voucher_SaleProductSearchRequest getSearchSaleProduct(@RequestBody Voucher_SaleProductSearchRequest voucherSearchRequest2) {
        saleProductSearchRequest = new Voucher_SaleProductSearchRequest(voucherSearchRequest2.getDiscountTypeCheck(),voucherSearchRequest2.getNameCheck(),voucherSearchRequest2.getStatusCheck());
        if(saleProductSearchRequest == null) {
            saleProductSearchRequest = new Voucher_SaleProductSearchRequest(null,"",1);
        }
        return saleProductSearchRequest;
    }

    @GetMapping("/reset-filter-sale-product")
    public String getResetFilterSaleProduct() {
        saleProductSearchRequest = new Voucher_SaleProductSearchRequest(null,"",1);
        return "done";
    }

    @PostMapping("/add-new-sale-product")
    public ResponseEntity<Map<String,String>> getMethodAddNewSaleProduct(@RequestBody SaleProductRequest saleProductRequest) {
        Map<String,String> thongBao = new HashMap<>();

        System.out.println(saleProductRequest.toString());

        saleProductService.createNewSale(saleProductRequest);
        thongBao.put("message","Thêm mới đợt giảm giá thành công!");
        thongBao.put("check","1");
        return ResponseEntity.ok(thongBao);
    }

    @GetMapping("/all-product/{page}")
    public List<Object[]> getAllProduct(@PathVariable("page") String page) {
        if(this.productDetailCheckMark2Request == null) {
            this.productDetailCheckMark2Request = new ProductDetailCheckMark2Request("",null,null,null,null,null,null,null,1);
        }
        Pageable pageable = PageRequest.of(Integer.parseInt(page)-1,5);
        return convertListToPage(this.saleProductService.getAllProduct(productDetailCheckMark2Request),pageable).getContent();
    }

    @GetMapping("/page-max-product")
    public Integer getMaxPageProduct(HttpSession session) {

        if(this.productDetailCheckMark2Request == null) {
            this.productDetailCheckMark2Request = new ProductDetailCheckMark2Request("",null,null,null,null,null,null,null,1);
        }

        Integer maxPageProduct = (int) Math.ceil((double) this.saleProductService.getAllProduct(this.productDetailCheckMark2Request).size() / 5);
        System.out.println("so trang cua san pham " + maxPageProduct);
        return maxPageProduct;
    }

    @PostMapping("/filter-product-deatail")
    public ResponseEntity<?> getFilterProduct(@RequestBody ProductDetailCheckMark2Request productDetailCheckRequest2, HttpSession session) {
        this.productDetailCheckMark2Request = productDetailCheckRequest2;
        System.out.println("Thong tin loc " + productDetailCheckRequest2.toString());
        return ResponseEntity.ok("Done");
    }

    ///////////////////////////////////////////////////
    protected Page<Object[]> convertListToPage(List<Object[]> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<Object[]> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }
}
