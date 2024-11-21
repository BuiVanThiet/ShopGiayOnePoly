package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.request.ProductInfoDto;
import com.example.shopgiayonepoly.dto.request.Statistics;
import com.example.shopgiayonepoly.dto.request.StatusBill;
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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
        return 1;
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

    @Override
    public Page<ProductInfoDto> getProductSalesPageByDateRange(int page, int size, String startDate, String endDate) {
        Pageable pageable = PageRequest.of(page, size);

        // Lấy dữ liệu từ repository, truyền vào startDate, endDate và Pageable
        Page<Object[]> productPage = chartRepository.getProductSalesPageByDateRange(pageable, startDate, endDate);

        // Chuyển đổi dữ liệu từ Object[] thành ProductInfoDto
        List<ProductInfoDto> productDtos = new ArrayList<>();
        for (Object[] row : productPage.getContent()) {
            ProductInfoDto productInfoDto = convertToProductInfoDto(row);
            productDtos.add(productInfoDto);
        }

        // Trả về một Page chứa danh sách ProductInfoDto
        return new PageImpl<>(productDtos, pageable, productPage.getTotalElements());
    }

    // Phương thức chuyển đổi từ Object[] thành ProductInfoDto
    private ProductInfoDto convertToProductInfoDto(Object[] row) {
        ProductInfoDto productInfoDto = new ProductInfoDto();

        // Lấy thông tin sản phẩm từ Object[]
        productInfoDto.setProductName((String) row[0]);
        productInfoDto.setColorName((String) row[1]);
        productInfoDto.setSizeName((String) row[2]);

        // Định dạng giá trị tiền tệ
        BigDecimal originalPrice = (BigDecimal) row[3];
        BigDecimal discountedPrice = (BigDecimal) row[4];

        // Định dạng tiền tệ (VND)
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String originalPriceFormatted = currencyFormatter.format(originalPrice).replace("₫", "VND");
        String promotionalPriceFormatted = currencyFormatter.format(discountedPrice).replace("₫", "VND");

        productInfoDto.setOriginalPrice(originalPriceFormatted);
        productInfoDto.setPromotionalPrice(promotionalPriceFormatted);

        // Lấy tổng số lượng bán được
        productInfoDto.setTotalQuantity((Integer) row[5]);

        // Chuyển đổi chuỗi hình ảnh từ STUFF thành danh sách hình ảnh
        String imageNames = (String) row[6];
        List<String> imageUrls = new ArrayList<>();
        if (imageNames != null && !imageNames.isEmpty()) {
            String[] images = imageNames.split(", ");
            for (String image : images) {
                // Kết hợp URL của Cloudinary với tên ảnh
                imageUrls.add("https://res.cloudinary.com/dfy4umpja/image/upload/v1728721025/" + image);
            }
        }
        productInfoDto.setImageUrls(imageUrls);

        return productInfoDto;
    }


    @Override
    public List<StatusBill> findBillsWithStatusDescription() {
        // Lấy kết quả từ repository
        List<Object[]> results = chartRepository.findBillsWithStatusDescription();

        // Khởi tạo danh sách kết quả
        List<StatusBill> statusBills = new ArrayList<>();

        // Duyệt qua từng kết quả trả về
        for (Object[] result : results) {
            // Lấy giá trị từ mảng Object[]
            String statusDescription = (String) result[0];  // Trạng thái (String)
            int countStatus = ((Number) result[1]).intValue();  // Số lượng trạng thái (int)

            // Thêm vào danh sách statusBills
            statusBills.add(new StatusBill(statusDescription, countStatus));
        }

        return statusBills;
    }

    @Override
    public List<StatusBill> getStatusCountForToday() {
        List<Object[]> results = chartRepository.getStatusCountForToday();

        // Khởi tạo danh sách kết quả
        List<StatusBill> statusBills = new ArrayList<>();

        // Duyệt qua từng kết quả trả về
        for (Object[] result : results) {
            // Lấy giá trị từ mảng Object[]
            String statusDescription = (String) result[0];  // Trạng thái (String)
            int countStatus = ((Number) result[1]).intValue();  // Số lượng trạng thái (int)

            // Thêm vào danh sách statusBills
            statusBills.add(new StatusBill(statusDescription, countStatus));
        }

        return statusBills;
    }

    @Override
    public List<StatusBill> findStatusCountsForLast7Days() {
        List<Object[]> results = chartRepository.findStatusCountsForLast7Days();

        // Khởi tạo danh sách kết quả
        List<StatusBill> statusBills = new ArrayList<>();

        // Duyệt qua từng kết quả trả về
        for (Object[] result : results) {
            // Lấy giá trị từ mảng Object[]
            String statusDescription = (String) result[0];  // Trạng thái (String)
            int countStatus = ((Number) result[1]).intValue();  // Số lượng trạng thái (int)

            // Thêm vào danh sách statusBills
            statusBills.add(new StatusBill(statusDescription, countStatus));
        }

        return statusBills;
    }

    @Override
    public List<StatusBill> countStatusByYear() {
        List<Object[]> results = chartRepository.countStatusByYear();

        // Khởi tạo danh sách kết quả
        List<StatusBill> statusBills = new ArrayList<>();

        // Duyệt qua từng kết quả trả về
        for (Object[] result : results) {
            // Lấy giá trị từ mảng Object[]
            String statusDescription = (String) result[0];  // Trạng thái (String)
            int countStatus = ((Number) result[1]).intValue();  // Số lượng trạng thái (int)

            // Thêm vào danh sách statusBills
            statusBills.add(new StatusBill(statusDescription, countStatus));
        }

        return statusBills;
    }

    @Override
    public List<Statistics> findStatisticsByDateRange(String startDate, String endDate) {
        List<Object[]> results = chartRepository.findStatisticsByDateRange(startDate, endDate);
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
    public List<StatusBill> getBillStatisticsByDateRange(String startDate, String endDate) {
        List<Object[]> results = chartRepository.getBillStatisticsByDateRange(startDate, endDate);
        List<StatusBill> statusBills = new ArrayList<>();

        for (Object[] result : results) {
            StatusBill statusBill = new StatusBill();
            statusBill.setStatusDescription((String) result[0]);
            statusBill.setCountStatus(((Number) result[1]).intValue());
            statusBills.add(statusBill);
        }

        return statusBills;
    }
}
