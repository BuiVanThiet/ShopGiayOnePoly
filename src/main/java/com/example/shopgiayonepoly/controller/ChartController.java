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
    public String form(Model model) {
        Long monthlyBill = chartService.monthlyBill();
        Long totalMonthlyBill = chartService.totalMonthlyBill();
        Long totalMonthlyInvoiceProducts = chartService.totalMonthlyInvoiceProducts();
        Long billOfTheDay = chartService.billOfTheDay();
        Long totalPriceToday = chartService.totalPriceToday();
        List<Date> findLastBillDates = chartService.findLastBillDates();

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedTotalMonthlyBill = numberFormat.format(totalMonthlyBill);
        String formattedTotalPriceToday = numberFormat.format(totalPriceToday);

        List<String> findLastDates = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Date date : findLastBillDates) {
            findLastDates.add(sdf.format(date));
        }

        model.addAttribute("monthlyBill", monthlyBill);

        model.addAttribute("totalMonthlyBill",formattedTotalMonthlyBill);

        model.addAttribute("totalMonthlyInvoiceProducts",totalMonthlyInvoiceProducts);

        model.addAttribute("billOfTheDay", billOfTheDay);

        model.addAttribute("totalPriceToday",formattedTotalPriceToday);

        model.addAttribute("findLastDates", findLastDates);

        List<ProductInfoDto> productSales = chartService.getProductSales();
        model.addAttribute("productSales", productSales);

        return "Charts/index";
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }
}
