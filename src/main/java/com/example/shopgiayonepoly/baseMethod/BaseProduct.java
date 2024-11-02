package com.example.shopgiayonepoly.baseMethod;

import com.example.shopgiayonepoly.repositores.ProductDetailRepository;
import com.example.shopgiayonepoly.repositores.ProductRepository;
import com.example.shopgiayonepoly.service.ProductService;
import com.example.shopgiayonepoly.service.attribute.*;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseProduct {


    @Autowired
    protected ProductService productService;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected ProductDetailRepository productDetailRepository;

    @Autowired
    protected MaterialService materialService;
    @Autowired
    protected OriginService originService;
    @Autowired
    protected ManufacturerService manufacturerService;
    @Autowired
    protected SoleService soleService;
    @Autowired
    protected CategoryService categoryService;
    @Autowired
    protected ColorService colorService;
    @Autowired
    protected SizeService sizeService;

}
