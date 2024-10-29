package com.example.shopgiayonepoly.controller;

import java.util.List;
import java.util.Map;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.service.BillService;
import com.example.shopgiayonepoly.service.CanvasjsChartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chart")
public class ChartController {
    @Autowired
    CanvasjsChartService canvasjsChartService;
    @Autowired
    BillService billService;

    @GetMapping("/form")
    public String form(Model model) {
        long paidBillCount = billService.invoicePaid();
        model.addAttribute("paidBillCount", paidBillCount);
        System.out.println(paidBillCount);
        return "/Charts/index";
    }

    @GetMapping("/")
    public String springMVC(ModelMap modelMap) {
        List<List<Map<Object, Object>>> canvasjsDataList = canvasjsChartService.getCanvasjsChartData();
        modelMap.addAttribute("dataPointsList", canvasjsDataList);
        return "/Charts/index";  // Đây là tên của tệp HTML (Thymeleaf) bạn đã tạo
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }
}
