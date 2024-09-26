package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.StaffRequest;
import com.example.shopgiayonepoly.dto.request.VoucherRequest;
import com.example.shopgiayonepoly.dto.response.StaffResponse;
import com.example.shopgiayonepoly.entites.Voucher;
import com.example.shopgiayonepoly.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    StaffService staffService;

    @GetMapping("/list")
    public String list(Model model) {
        List<StaffResponse> listStaff = staffService.getAllStaff();
        model.addAttribute("staffList", staffService.getAllStaff());
//        System.out.println(staffService.findAll());
        return "Staff/list";
    }

    @GetMapping("/search")
    public String searchStaffByKey(@RequestParam(name = "key") String key, Model model) {
        List<StaffResponse> searchStaff = staffService.searchStaffByKeyword(key);
        model.addAttribute("staffList", searchStaff);
        model.addAttribute("staff", new StaffRequest());
        return "staff/list";
    }

}
