package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.StaffRequest;
import com.example.shopgiayonepoly.dto.request.VoucherRequest;
import com.example.shopgiayonepoly.dto.response.StaffResponse;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.entites.Voucher;
import com.example.shopgiayonepoly.service.StaffService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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
        return "Staff/list";
    }

    @GetMapping("/search")
    public String searchStaffByKey(@RequestParam(name = "key") String key, Model model) {
        List<StaffResponse> searchStaff = staffService.searchStaffByKeyword(key);
        model.addAttribute("staffList", searchStaff);
        model.addAttribute("staff", new StaffRequest());
        return "Staff/list";
    }

    @GetMapping("/create")
    public String createStaff(ModelMap modelMap){
        modelMap.addAttribute("staff",new StaffRequest());
        return "Staff/create";
    }

    @PostMapping("/add")
    public String addStaff(Model model, @ModelAttribute(name="staff") StaffRequest staffRequest){
        System.out.println("Du lieu khi them cua staff: " + staffRequest.toString());
        Staff staff = new Staff();
        staff.setCodeStaff(staffRequest.getCodeStaff());
        staff.setFullName(staffRequest.getFullName());
        staff.setAddress(staffRequest.getCommune() + "," + staffRequest.getDistrict() + "," + staffRequest.getCity() + "," +staffRequest.getAddRessDetail());
        staff.setNumberPhone(staffRequest.getNumberPhone());
        staff.setBirthDay(staffRequest.getBirthDay());
        staff.setImage(staffRequest.getNameImage());
        staff.setEmail(staffRequest.getEmail());
        staff.setAcount("");
        staff.setPassword("");
        staff.setGender(staffRequest.getGender());
        staff.setRole(staffRequest.getRole());
        staff.setStatus(staffRequest.getStatus());
        Staff staffSave = this.staffService.save(staff);
        staffSave.setAcount(staffSave.getCodeStaff()+staffSave.getId());
        staff.setPassword("@shoponepoly");
        this.staffService.save(staffSave);
        return "redirect:/staff/create";
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session){
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }

}
