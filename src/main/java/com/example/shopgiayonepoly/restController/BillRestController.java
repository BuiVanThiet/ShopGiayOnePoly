package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.entites.*;
import com.example.shopgiayonepoly.implement.BillDetailImplement;
import com.example.shopgiayonepoly.implement.BillImplement;
import com.example.shopgiayonepoly.implement.ProductDetailImplement;
import com.example.shopgiayonepoly.repositores.ProductRepository;
import com.example.shopgiayonepoly.repositores.SaleProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bill-api")
public class BillRestController {
    @Autowired
    BillImplement billImplement;
    @Autowired
    ProductDetailImplement productDetailImplement;
    @Autowired
    SaleProductRepository saleProductRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    BillDetailImplement billDetailImplement;
    @GetMapping("/all")
    public List<Bill> getAll() {
        return billImplement.findAll();
    }
    @GetMapping("/product-detail-all")
    public List<ProductDetail> getAllProductDetail() {
        return productDetailImplement.findAll();
    }
    @GetMapping("/sale-product-all")
    public List<SaleProduct> getAllSaleProduct() {
        return saleProductRepository.findAll();
    }
    @GetMapping("/product-all")
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }
    @GetMapping("/all-new")
    public List<Bill> getAllNew() {
        return billImplement.getBillByStatusNew();
    }
    @GetMapping("/bill-detail-by-id-bill")
    public List<BillDetail> getBillDetail(HttpSession session) {
//        Integer idBill = (Integer) session.getAttribute("idBillSS");
//        return billDetailImplement.getBillDetailByIdBill(idBill);
//        if(idBill == null) {
//            System.out.printf("dang null");
//        }
        System.out.println("session get id rest: "+session.getId());

//        System.out.println("session la " + session.getAttribute("BillId"));
        System.out.println("id bill la: " + session.getAttribute("IdBill"));
        return this.billDetailImplement.getBillDetailByIdBill((Integer) session.getAttribute("IdBill"));
    }


}
