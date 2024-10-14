package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.request.ProductDetailDiscountRequest;
import com.example.shopgiayonepoly.dto.request.SaleProductRequest;
import com.example.shopgiayonepoly.dto.response.ProductWithDiscountResponse;
import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.entites.SaleProduct;
import com.example.shopgiayonepoly.repositores.ProductDetailRepository;
import com.example.shopgiayonepoly.repositores.SaleProductRepository;
import com.example.shopgiayonepoly.repositores.attribute.ColorRepository;
import com.example.shopgiayonepoly.service.SaleProductService;
import com.example.shopgiayonepoly.service.TemporaryPriceStorage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleProductServiceImplement implements SaleProductService {
    @Autowired
    private SaleProductRepository saleProductRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private TemporaryPriceStorage temporaryPriceStorage;

    @Override
    public Page<SaleProduct> getAllSaleProductByPage(Pageable pageable) {
        return saleProductRepository.getAllSaleProductByPage(pageable);
    }

    @Override
    public List<SaleProduct> getAllSaleProducts() {
        return saleProductRepository.getAllSaleProduct();
    }

    @Override
    public Page<SaleProduct> getDeletedSaleProductsByPage(Pageable pageable) {
        return saleProductRepository.getSaleProductDeleteByPage(pageable);
    }

    @Override
    public List<SaleProduct> getAllDeletedSaleProducts() {
        return saleProductRepository.getAllSaleProductDelete();
    }

    @Override
    public void deleteSaleProductBySetStatus(Integer id) {
        saleProductRepository.deleteBySetStatus(id);
    }

    @Override
    public void restoreSaleProductStatus(Integer id) {
        saleProductRepository.restoreStatusSaleProduct(id);
    }

    @Override
    public Page<SaleProduct> searchSaleProductsByKeyword(String key, Pageable pageable) {
        return saleProductRepository.searchSaleProductByKeyword(key, pageable);
    }

    @Override
    public Page<SaleProduct> searchSaleProductsByType(int type, Pageable pageable) {
        return saleProductRepository.searchSaleProductByTypeSaleProduct(type, pageable);
    }

    @Override
    public List<ProductDetail> getAllProductDetailByPage() {
        return saleProductRepository.getAllProductDetailByPage();
    }

    @Override
    public List<ProductDetail> getAllProductDetailWithDiscount() {
       return saleProductRepository.getAllProductDetailWithDiscount();
    }

    @Override
    public void createNewSale(SaleProductRequest saleProductRequest) {
        SaleProduct saleProduct = new SaleProduct();
        BeanUtils.copyProperties(saleProductRequest, saleProduct);
        saleProductRepository.save(saleProduct);
    }

    @Override
    public SaleProduct getSaleProductByID(Integer id) {
        return saleProductRepository.findById(id).orElse(new SaleProduct());
    }

    @Override
    public void applyDiscountToProductDetails(List<Integer> productIds,
                                              BigDecimal discountValue,
                                              Integer discountType,
                                              Integer saleProductId) {
        System.out.println("productIds: " + productIds);
        System.out.println("discountValue: " + discountValue);
        System.out.println("discountType: " + discountType);
        System.out.println("saleProductId: " + saleProductId);

        SaleProduct saleProduct = saleProductRepository.findById(saleProductId).orElse(null);

        if (saleProduct == null) {
            throw new IllegalArgumentException("SaleProduct không tồn tại với ID: " + saleProductId);
        }

        for (Integer productId : productIds) {
            ProductDetail product = productDetailRepository.findById(productId).orElse(null);
            if (product != null) {
                // Lưu giá gốc
                BigDecimal originalPrice = product.getPrice();
                temporaryPriceStorage.storeOriginalPrice(product.getId(), originalPrice);

                BigDecimal newPrice;
                if (discountType == 1) {
                    newPrice = originalPrice.subtract(originalPrice.multiply(discountValue).divide(BigDecimal.valueOf(100)));
                } else {
                    newPrice = originalPrice.subtract(discountValue);
                }
                product.setPrice(newPrice);
                productDetailRepository.save(product);
            }
        }

    }


    @Override
    public void restoreOriginalPrice(List<Integer> productIds) {
        for (Integer productId : productIds) {
            ProductDetail product = productDetailRepository.findById(productId).orElse(null);

            if (product != null) {
                SaleProduct saleProduct = product.getSaleProduct();

                if (saleProduct != null) {
                    BigDecimal discountValue = saleProduct.getDiscountValue(); // Giảm giá (có thể là phần trăm hoặc giá trị)
                    int discountType = saleProduct.getDiscountType(); // Loại giảm giá (1: phần trăm, 2: giá trị cố định)
                    BigDecimal originalPrice;

                    if (discountType == 1) {
                        originalPrice = product.getPrice().divide(BigDecimal.ONE.subtract(discountValue.divide(BigDecimal.valueOf(100))), RoundingMode.HALF_UP);
                    } else if (discountType == 2) {
                        // Nếu là giảm giá theo giá trị cố định
                        originalPrice = product.getPrice().add(discountValue);
                    } else {
                        originalPrice = product.getPrice();
                    }


                    product.setPrice(originalPrice);
                    product.setSaleProduct(null);
                    productDetailRepository.save(product);

                    System.out.println("Giá gốc đã được khôi phục cho sản phẩm ID " + productId + ": " + originalPrice);
                } else {
                    System.out.println("Sản phẩm ID " + productId + " không có đợt giảm giá.");
                }
            } else {
                System.out.println("Sản phẩm ID " + productId + " không tồn tại.");
            }
        }
    }




    private String generateSaleCode() {
        return "SALE" + System.currentTimeMillis();
    }


}
