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
//            staffProfile.setAccount(staff.getAcount());
            staffProfile.setFullName(staff.getFullName());
//            staffProfile.setPassword(staff.getPassword());
            staffProfile.setEmail(staff.getEmail());
            staffProfile.setNumberPhone(staff.getNumberPhone());
            staffProfile.setGender(staff.getGender());
            staffProfile.setBirthDay(staff.getBirthDay());
            staffProfile.setImageStaffString(staff.getImage());

            String[] partStaff = staff.getAddress().split(",\\s*");
            staffProfile.setProvince(partStaff[2]);
            staffProfile.setDistrict(partStaff[1]);
            staffProfile.setWard(partStaff[0]);
            staffProfile.setAddRessDetail(String.join(", ", java.util.Arrays.copyOfRange(partStaff, 3, partStaff.length)));

            System.out.println("hiển thị :"+ staff.toString());
            System.out.println(staffProfile.toString());
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
    public String updateStaffProfile(@Valid @ModelAttribute("staffProfile") StaffProfileRequest staffProfile,
                                     BindingResult bindingResult,
                                     @RequestParam("nameImageStaff") MultipartFile nameImage,
                                     HttpSession session,
                                     Model model) throws IOException {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        System.out.println("Staff từ model: " + staff);

        if(bindingResult.hasErrors()){
            model.addAttribute("staffProfile", staffProfile);
            return "Profile/staff_profile";
        }
        if (staff != null) {

            // Cập nhật thông tin staff dựa trên dữ liệu từ form
            staff.setEmail(staffProfile.getEmail());
            staff.setNumberPhone(staffProfile.getNumberPhone());
            staff.setGender(staffProfile.getGender());
            staff.setBirthDay(staffProfile.getBirthDay());
            staff.setAddress(staffProfile.getWard() + "," + staffProfile.getDistrict() + "," + staffProfile.getProvince() + "," + staffProfile.getAddRessDetail());
            // Lưu staff
            staffService.save(staff);
            System.out.println("Sau khi lưu: " + staff.toString());

            // Kiểm tra nếu có ảnh mới
            if (!nameImage.isEmpty()) {
                // Tải ảnh mới lên
                String imageId = staffService.uploadFile(nameImage, staff.getId());
                String randomImageName = extractRandomImageName(imageId); // Lấy tên ngẫu nhiên từ đường dẫn
                staff.setImage(randomImageName); // Cập nhật tên ảnh vào staff
                System.out.println("Đường dẫn ảnh mới: " + imageId);
                System.out.println("Tên ảnh ngẫu nhiên: " + randomImageName);
            } else {
                // Nếu không có ảnh mới, giữ nguyên ảnh cũ
                System.out.println("Không có ảnh mới, giữ nguyên ảnh cũ: " + staff.getImage());
            }

            model.addAttribute("staffProfile", staffProfile);
            model.addAttribute("successMessage", "Cập nhật thông tin thành công.");
        } else {
            model.addAttribute("errorMessage", "Không tìm thấy thông tin tài khoản nhân viên.");
        }

        return "redirect:/profile/staffProfile"; // Chuyển hướng sau khi cập nhật
    }

    // Phương thức để trích xuất tên ngẫu nhiên từ URL
    private String extractRandomImageName(String url) {
        // Tách phần cuối của URL để lấy tên ảnh
        String[] parts = url.split("/");
        String fileNameWithExtension = parts[parts.length - 1]; // lấy phần cuối
        String fileName = fileNameWithExtension.split("\\.")[0]; // tách phần mở rộng

        return fileName; // trả về tên ảnh ngẫu nhiên
    }

    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session){
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }

}
