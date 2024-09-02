package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.entites.Bill;
import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.entites.SaleProduct;
import com.example.shopgiayonepoly.implement.BillImplement;
import com.example.shopgiayonepoly.implement.ProductDetailImplement;
import com.example.shopgiayonepoly.repositores.SaleProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @GetMapping("/all")
    public List<Bill> getAll() {
        return billImplement.findAll();
    }
    @GetMapping("/product-all")
    public List<ProductDetail> getAllProduct() {
        return productDetailImplement.findAll();
    }
    @GetMapping("/sale-product-all")
    public List<SaleProduct> getAllSaleProduct() {
        return saleProductRepository.findAll();
    }

}
