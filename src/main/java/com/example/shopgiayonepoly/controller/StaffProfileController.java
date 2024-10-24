package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.StaffProfileRequest;
import com.example.shopgiayonepoly.dto.request.UserProfileUpdateRequest;
import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.repositores.StaffRepository;
import com.example.shopgiayonepoly.service.StaffService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/profile")
public class StaffProfileController {
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    StaffService staffService;
    @GetMapping("/staffProfile")
    public String formStaffProfile(Model model) {
        Staff staff = (Staff) model.getAttribute("staffInfo");

        if (staff != null) {
            // Bạn có thể sử dụng thông tin staff để thực hiện các thao tác khác
            StaffProfileRequest staffProfile = new StaffProfileRequest();
            staffProfile.setAccount(staff.getAcount());
            staffProfile.setFullName(staff.getFullName());
            staffProfile.setPassword(staff.getPassword());
            staffProfile.setEmail(staff.getEmail());
            staffProfile.setNumberPhone(staff.getNumberPhone());
            staffProfile.setGender(staff.getGender());
            staffProfile.setBirthDay(staff.getBirthDay());

            String[] partStaff = staff.getAddress().split(",\\s*");
            staffProfile.setProvince(partStaff[2]);
            staffProfile.setDistrict(partStaff[1]);
            staffProfile.setWard(partStaff[0]);
            staffProfile.setAddRessDetail(String.join(", ", java.util.Arrays.copyOfRange(partStaff, 3, partStaff.length)));
            staffProfile.setImageStaffString(staff.getImage());
            System.out.println("hiển thị"+ staff.toString());
            LocalDate birthDayStaff = staff.getBirthDay();
            if (birthDayStaff != null) {
                model.addAttribute("birthDayDay", birthDayStaff.getDayOfMonth());
                model.addAttribute("birthDayMonth", birthDayStaff.getMonthValue());
                model.addAttribute("birthDayYear", birthDayStaff.getYear());
            } else {
                model.addAttribute("birthDayDay", "");
                model.addAttribute("birthDayMonth", "");
                model.addAttribute("birthDayYear", "");
            }

            model.addAttribute("staffProfile", staffProfile);
        } else {
            model.addAttribute("errorMessage", "Không tìm thấy thông tin tài khoản nhân viên.");
            return "login/login"; // Chuyển hướng về trang đăng nhập nếu chưa có thông tin
        }

        return "Profile/staff_profile";
    }

    @PostMapping("/updateStaffProfile")
    public String updateStaffProfile(StaffProfileRequest staffProfile,
                                     HttpSession session,
                                     @RequestParam("nameImageStaff") MultipartFile nameImage,
                                     Model model) throws IOException {
        Staff staff = (Staff) session.getAttribute("staffLogin");

        if (staff != null) {
            // Cập nhật thông tin staff dựa trên dữ liệu từ form
            staff.setFullName(staffProfile.getFullName());
            staff.setPassword(staffProfile.getPassword());
            staff.setEmail(staffProfile.getEmail());
            staff.setNumberPhone(staffProfile.getNumberPhone());
            staff.setGender(staffProfile.getGender());
            staff.setBirthDay(staffProfile.getBirthDay());
            staff.setAddress(staffProfile.getWard() + "," + staffProfile.getDistrict() + "," + staffProfile.getProvince() + "," + staffProfile.getAddRessDetail());
            staffService.save(staff);
            // Kiểm tra nếu có ảnh mới
            if (!nameImage.isEmpty()) {
                // Tải ảnh mới lên
                String imageId = staffService.uploadFile(nameImage, staff.getId());
                staff.setImage(imageId);
                // Nếu không nhập ảnh thì lấy ảnh cũ cập nhật vào
            }else {
                staff.setImage(staff.getImage());
            }
            System.out.println(staff.toString());
            model.addAttribute("staffProfile", staffProfile);
            model.addAttribute("successMessage", "Cập nhật thông tin thành công.");
        } else {
            model.addAttribute("errorMessage", "Không tìm thấy thông tin tài khoản nhân viên.");
        }

        return "redirect:/profile/staffProfile"; // Chuyển hướng sau khi cập nhật
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session){
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }

}
