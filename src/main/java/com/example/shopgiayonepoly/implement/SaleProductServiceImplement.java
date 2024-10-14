package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.request.ProductDetailDiscountRequest;
import com.example.shopgiayonepoly.dto.request.SaleProductRequest;
import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.entites.SaleProduct;
import com.example.shopgiayonepoly.repositores.ProductDetailRepository;
import com.example.shopgiayonepoly.repositores.SaleProductRepository;
import com.example.shopgiayonepoly.repositores.attribute.ColorRepository;
import com.example.shopgiayonepoly.service.SaleProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleProductServiceImplement implements SaleProductService {
    @Autowired
    private SaleProductRepository saleProductRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private ColorRepository colorRepository;

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
        // Tìm kiếm SaleProduct theo saleProductId
        SaleProduct saleProduct = saleProductRepository.findById(saleProductId).orElse(null);

        if (saleProduct == null) {
            throw new IllegalArgumentException("SaleProduct không tồn tại với ID: " + saleProductId);
        }

        List<ProductDetailDiscountRequest> productDetailRequests = new ArrayList<>();

        for (Integer productId : productIds) {
            ProductDetail product = productDetailRepository.findById(productId).orElse(null);
            if (product != null) {
                ProductDetailDiscountRequest productRequest = new ProductDetailDiscountRequest();
                productRequest.setId(product.getId());

                // Lưu giá gốc
                BigDecimal originalPrice = product.getPrice(); // Lưu giá hiện tại trước khi thay đổi
                productRequest.setOriginalPrice(originalPrice);

                // Tính toán giá mới
                BigDecimal newPrice;
                if (discountType == 1) {
                    newPrice = originalPrice.subtract(originalPrice.multiply(discountValue).divide(BigDecimal.valueOf(100)));
                } else {
                    newPrice = originalPrice.subtract(discountValue);
                }
                productRequest.setNewPrice(newPrice);
                productRequest.setSaleProduct(saleProduct);
                productDetailRequests.add(productRequest);
                product.setPrice(newPrice);
                product.setSaleProduct(saleProduct);
                productDetailRepository.save(product);
            }
        }
    }
    @Override
    public void restoreOriginalPrice(List<ProductDetailDiscountRequest> productDetailRequests) {
        for (ProductDetailDiscountRequest request : productDetailRequests) {
            ProductDetail product = productDetailRepository.findById(request.getId()).orElse(null);
            if (product != null) {
                // Lấy giá gốc
                BigDecimal originalPrice = request.getOriginalPrice();

                if (originalPrice != null) {
                    product.setPrice(originalPrice);
                    product.setSaleProduct(null);
                    productDetailRepository.save(product);
                }
            }
        }
    }

    private BigDecimal getOriginalPriceFromHistory(Integer productId,
                                                   List<ProductDetailDiscountRequest> productDetailRequests) {
        for (ProductDetailDiscountRequest request : productDetailRequests) {
            if (request.getId().equals(productId)) {
                return request.getNewPrice();
            }
        }
        return null;
    }




    private String generateSaleCode() {
        return "SALE" + System.currentTimeMillis(); // Tạo một mã giảm giá dựa trên thời gian hiện tại
    }


}
