package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.request.ProductInfoDto;
import com.example.shopgiayonepoly.dto.request.Statistics;
import com.example.shopgiayonepoly.repositores.ChartRepository;
import com.example.shopgiayonepoly.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

@Service
public class ChartImplement implements ChartService {
    @Autowired
    ChartRepository chartRepository;

    @Override
    public long monthlyBill() {
        return chartRepository.monthlyBill();
    }

    @Override
    public long totalMonthlyBill() {
        return chartRepository.totalMonthlyBill();
    }

    @Override
    public long totalMonthlyInvoiceProducts() {
        return chartRepository.totalMonthlyInvoiceProducts();
    }

    @Override
    public long billOfTheDay() {
        return chartRepository.billOfTheDay();
    }

    @Override
    public long totalPriceToday() {
        return chartRepository.totalPriceToday();
    }

    @Override
    public List<Date> findLastBillDates() {
        return chartRepository.findLastBillDates();
    }

    @Override
    public List<Statistics> findMonthlyStatistics() {
        List<Object[]> results = chartRepository.findMonthlyStatistics();
        List<Statistics> statistics = new ArrayList<>();

        for (Object[] result : results) {
            String month = (String) result[0];
            int invoiceCount = ((Number) result[1]).intValue();
            int productCount = ((Number) result[2]).intValue();

            statistics.add(new Statistics(month, invoiceCount, productCount));
        }

        return statistics; // Trả về danh sách các đối tượng DTO
    }

    @Override
    public List<Statistics> findTodayStatistics() {
        List<Object[]> results = chartRepository.findTodayStatistics();
        List<Statistics> statistics = new ArrayList<>();

        for (Object[] result : results) {
            String month = (String) result[0];
            int invoiceCount = ((Number) result[1]).intValue();
            int productCount = ((Number) result[2]).intValue();

            statistics.add(new Statistics(month, invoiceCount, productCount));
        }

        return statistics;
    }

    @Override
    public List<Statistics> findLast7DaysStatistics() {
        List<Object[]> results = chartRepository.findLast7DaysStatistics();
        List<Statistics> statistics = new ArrayList<>();

        for (Object[] result : results) {
            String month = (String) result[0];
            int invoiceCount = ((Number) result[1]).intValue();
            int productCount = ((Number) result[2]).intValue();

            statistics.add(new Statistics(month, invoiceCount, productCount));
        }

        return statistics;
    }

    @Override
    public List<Statistics> getAnnualStatistics() {
        List<Object[]> results = chartRepository.getAnnualStatistics();
        List<Statistics> statistics = new ArrayList<>();

        for (Object[] result : results) {
            String month = (String) result[0];
            int invoiceCount = ((Number) result[1]).intValue();
            int productCount = ((Number) result[2]).intValue();

            statistics.add(new Statistics(month, invoiceCount, productCount));
        }

        return statistics;
    }

    @Override
    public List<ProductInfoDto> getProductSales() {
        List<Object[]> result = chartRepository.getProductSales();
        List<ProductInfoDto> productSales = new ArrayList<>();

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (Object[] row : result) {
            String productName = (String) row[0];
            String colorName = (String) row[1];
            String sizeName = (String) row[2];
            BigDecimal OriginalPrice = (BigDecimal) row[3];
            BigDecimal discountedPrice = (BigDecimal) row[4];
            int totalQuantity = (int) row[5];
            String imageNames = (String) row[6]; // Chuỗi chứa tên ảnh, phân cách bởi dấu phẩy

            String originalPrice = currencyFormatter.format(OriginalPrice).replace("₫", "VND");
            String promotionalPrice = currencyFormatter.format(discountedPrice).replace("₫", "VND");

            // Tạo danh sách URL ảnh
            List<String> imageUrls = new ArrayList<>();
            if (imageNames != null && !imageNames.isEmpty()) {
                String[] images = imageNames.split(", ");
                for (String image : images) {
                    // Kết hợp URL cố định của Cloudinary với tên ảnh
                    imageUrls.add("https://res.cloudinary.com/dfy4umpja/image/upload/v1728721025/" + image);
                }
            }

            // Tạo đối tượng ProductInfoDto và thêm vào danh sách
            ProductInfoDto productInfoDto = new ProductInfoDto(
                    productName, colorName, sizeName, originalPrice, promotionalPrice, totalQuantity, imageUrls);

            productSales.add(productInfoDto); // Thêm đối tượng vào danh sách
        }

        return productSales; // Trả về danh sách sản phẩm
    }

    @Override
    public Page<ProductInfoDto> getProductSalesPage(int page, int size) {
        // Tạo pageable với số trang và số sản phẩm trên mỗi trang
        Pageable pageable = PageRequest.of(page, size);

        // Lấy kết quả phân trang từ repository
        Page<Object[]> productPage = chartRepository.getProductSalesPage(pageable);

        // Định dạng tiền tệ cho Việt Nam
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        // Chuyển đổi Object[] sang ProductInfoDto
        List<ProductInfoDto> productDtos = new ArrayList<>();
        for (Object[] row : productPage.getContent()) {
            ProductInfoDto productInfoDto = new ProductInfoDto();

            // Lấy thông tin sản phẩm và gán vào DTO
            productInfoDto.setProductName((String) row[0]);
            productInfoDto.setColorName((String) row[1]);
            productInfoDto.setSizeName((String) row[2]);

            // Định dạng giá trị tiền tệ
            BigDecimal originalPrice = (BigDecimal) row[3];
            BigDecimal discountedPrice = (BigDecimal) row[4];
            String originalPriceFormatted = currencyFormatter.format(originalPrice).replace("₫", "VND");
            String promotionalPriceFormatted = currencyFormatter.format(discountedPrice).replace("₫", "VND");

            productInfoDto.setOriginalPrice(originalPriceFormatted);
            productInfoDto.setPromotionalPrice(promotionalPriceFormatted);

            productInfoDto.setTotalQuantity((Integer) row[5]);

            // Chuyển đổi chuỗi hình ảnh từ `STUFF` thành danh sách hình ảnh
            String imageNames = (String) row[6];
            List<String> imageUrls = new ArrayList<>();
            if (imageNames != null && !imageNames.isEmpty()) {
                String[] images = imageNames.split(", ");
                for (String image : images) {
                    // Kết hợp URL cố định của Cloudinary với tên ảnh
                    imageUrls.add("https://res.cloudinary.com/dfy4umpja/image/upload/v1728721025/" + image);
                }
            }
            productInfoDto.setImageUrls(imageUrls);

            // Thêm đối tượng ProductInfoDto vào danh sách
            productDtos.add(productInfoDto);
        }

        // Trả về page chứa danh sách DTO
        return new PageImpl<>(productDtos, pageable, productPage.getTotalElements());
    }

}
