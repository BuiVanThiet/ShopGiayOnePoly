package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.request.SaleProductRequest;
import com.example.shopgiayonepoly.dto.request.VoucherRequest;
import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.entites.SaleProduct;
import com.example.shopgiayonepoly.repositores.ProductDetailRepository;
import com.example.shopgiayonepoly.repositores.SaleProductRepository;
import com.example.shopgiayonepoly.service.SaleProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class SaleProductServiceImplement implements SaleProductService {
    @Autowired
    private SaleProductRepository saleProductRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;

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
    public void applyDiscountToProductDetails(List<Integer> productIds, BigDecimal discountValue, Integer discountType, Integer saleProductId) {
        // Tìm kiếm SaleProduct theo saleProductId
        SaleProduct saleProduct = saleProductRepository.findById(saleProductId).orElse(null);

        if (saleProduct == null) {
            throw new IllegalArgumentException("SaleProduct không tồn tại với ID: " + saleProductId);
        }

        for (Integer productId : productIds) {
            ProductDetail product = productDetailRepository.findById(productId).orElse(null);
            if (product != null) {
                // Lưu giá gốc nếu chưa lưu
                if (product.getOriginalPrice() == null) {
                    product.setOriginalPrice(product.getPrice());
                }

                // Tính toán giá mới sau khi giảm giá
                BigDecimal newPrice;
                if (discountType == 1) {
                    // Giảm theo tỷ lệ phần trăm
                    newPrice = product.getPrice().subtract(product.getPrice().multiply(discountValue).divide(BigDecimal.valueOf(100)));
                } else {
                    // Giảm theo giá trị cố định
                    newPrice = product.getPrice().subtract(discountValue);
                }
                if(product.getSaleProduct()!=null){
                    break;
                }
                product.setPrice(newPrice);

                // Liên kết SaleProduct đã chọn với ProductDetail
                product.setSaleProduct(saleProduct);

                // Lưu ProductDetail sau khi cập nhật
                productDetailRepository.save(product);
            }
        }
    }

    @Override
    public void restoreOriginalPrice(List<Integer> productIds) {
        for (Integer productId : productIds) {
            ProductDetail product = productDetailRepository.findById(productId).orElse(null);
            if (product != null && product.getOriginalPrice() != null) {
                // Khôi phục giá gốc
                product.setPrice(product.getOriginalPrice());
                // Xóa liên kết SaleProduct
                product.setSaleProduct(null);
                product.setOriginalPrice(null);
                productDetailRepository.save(product);
            }
        }
    }

    private String generateSaleCode() {
        return "SALE" + System.currentTimeMillis(); // Tạo một mã giảm giá dựa trên thời gian hiện tại
    }


}
