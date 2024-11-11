package com.example.shopgiayonepoly.controller;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.shopgiayonepoly.dto.request.ProductInfoDto;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.CanvasjsChartService;
import com.example.shopgiayonepoly.service.ChartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/chart")
public class ChartController {
    @Autowired
    ChartService chartService;

    @GetMapping("/form")
    public String form(Model model, @RequestParam(defaultValue ="0") int page) {
        long monthlyBill = chartService.monthlyBill();
        long totalMonthlyBill = chartService.totalMonthlyBill();
        long totalMonthlyInvoiceProducts = chartService.totalMonthlyInvoiceProducts();
        long billOfTheDay = chartService.billOfTheDay();
        long totalPriceToday = chartService.totalPriceToday();
        List<Date> findLastBillDates = chartService.findLastBillDates();
        // chuyển tiền sang VND
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedTotalMonthlyBill = numberFormat.format(totalMonthlyBill);
        String formattedTotalPriceToday = numberFormat.format(totalPriceToday);

        List<String> findLastDates = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Date date : findLastBillDates) {
            findLastDates.add(sdf.format(date));
        }

        // Hóa đơn tháng này
        model.addAttribute("monthlyBill", monthlyBill);
        // Tổng tiền hóa đơn tháng này
        model.addAttribute("totalMonthlyBill",formattedTotalMonthlyBill);
        // Số sản phẩm tháng này
        model.addAttribute("totalMonthlyInvoiceProducts",totalMonthlyInvoiceProducts);
        // Số hóa đơn hôm nay
        model.addAttribute("billOfTheDay", billOfTheDay);
        // Tổng giá sản phẩm đã bán trong hôm nay
        model.addAttribute("totalPriceToday",formattedTotalPriceToday);

        model.addAttribute("findLastDates", findLastDates);
//danh sách sp chưa phân trang
        List<ProductInfoDto> productSales = chartService.getProductSales();
        model.addAttribute("productSales", productSales);
//danh sách sp phân trang

        Page<ProductInfoDto> productPage = chartService.getProductSalesPage(page, 3);
        model.addAttribute("productPage", productPage);

        return "Charts/index";
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }
}
