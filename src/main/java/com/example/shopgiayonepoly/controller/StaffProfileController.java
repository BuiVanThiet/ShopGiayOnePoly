package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.StaffProfileRequest;
import com.example.shopgiayonepoly.dto.request.UserProfileUpdateRequest;
import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.repositores.StaffRepository;
import com.example.shopgiayonepoly.service.CashierInventoryService;
import com.example.shopgiayonepoly.service.CustomerService;
import com.example.shopgiayonepoly.service.StaffService;
import com.example.shopgiayonepoly.service.TimekeepingService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/profile")
public class StaffProfileController {
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    StaffService staffService;
    @Autowired
    CustomerService customerService;

    @Autowired
    TimekeepingService timekeepingService;
    @Autowired
    CashierInventoryService cashierInventoryService;

    String mess ="";
    String check ="";
    @GetMapping("/staffProfile")
    public String formStaffProfile(Model model) {
        Staff staff = (Staff) model.getAttribute("staffInfo");

        if (staff != null) {
            StaffProfileRequest staffProfile = new StaffProfileRequest();
            staffProfile.setFullName(staff.getFullName());
            staffProfile.setEmail(staff.getEmail());
            staffProfile.setNumberPhone(staff.getNumberPhone());
            staffProfile.setGender(staff.getGender());
            staffProfile.setBirthDay(staff.getBirthDay());
            staffProfile.setImageStaffString(staff.getImage());

            staffProfile.setStatus(staff.getStatus());

            String[] partStaff = staff.getAddress().split(",\\s*");
            staffProfile.setProvince(partStaff[2]);
            staffProfile.setDistrict(partStaff[1]);
            staffProfile.setWard(partStaff[0]);
            staffProfile.setAddRessDetail(String.join(", ", java.util.Arrays.copyOfRange(partStaff, 3, partStaff.length)));

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
            model.addAttribute("check",check);
            model.addAttribute("mess",mess);
            model.addAttribute("staffProfile", staffProfile);
            mess ="";
            check ="";
        } else {
            model.addAttribute("errorMessage", "Không tìm thấy thông tin tài khoản nhân viên.");
            return "login/login";
        }

        return "Profile/staff_profile";
    }

    @PostMapping("/UpdateStaffProfile")
    public String updateStaffProfile(@Valid @ModelAttribute("staffProfile") StaffProfileRequest staffProfile,
                                     BindingResult bindingResult,
                                     @RequestParam("nameImageStaff") MultipartFile nameImage,
                                     HttpSession session,
                                     Model model) throws IOException {
        Staff staff = (Staff) session.getAttribute("staffLogin");

        if (staff == null) {
            return "redirect:/login";
        }

        if(staff.getStatus() != 1) {
            return "redirect:/home_manage";
        }

        staffProfile.setImageStaffString(staff.getImage());

        if (staffProfile.getFullName() == null || staffProfile.getFullName().trim().isEmpty()) {
            bindingResult.rejectValue("fullName", "error.staffProfile", "Họ và tên không được để trống");

        } else if (!staffProfile.getFullName().matches("^[\\p{L}\\s]+$")) {
            bindingResult.rejectValue("fullName", "error.staffProfile", "Họ và tên chỉ được nhập chữ cái");

        } else if (staffProfile.getFullName().length() < 5 || staffProfile.getFullName().length() > 255) {
            bindingResult.rejectValue("fullName", "error.staffProfile", "Họ và tên phải có độ dài từ 5 đến 255 ký tự");

        }

        if (staffProfile.getEmail() == null || staffProfile.getEmail().isEmpty()) {
            bindingResult.rejectValue("email", "error.staffProfile", "Email không được để trống");
        } else if (!staffProfile.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            bindingResult.rejectValue("email", "error.staffProfile", "Email không hợp lệ");
        } else if (staffProfile.getEmail().length() > 100) {
            bindingResult.rejectValue("email", "error.staffProfile", "Email không vượt quá 100 ký tự");
        } else {
            boolean emailExistsInStaff = staffService.existsByEmail(staffProfile.getEmail()) != null;
            boolean emailExistsInCustomer = customerService.existsByEmail(staffProfile.getEmail()) != null;
            if (!staffProfile.getEmail().equals(staff.getEmail()) && (emailExistsInStaff || emailExistsInCustomer)) {
                bindingResult.rejectValue("email", "error.staffProfile", "Email đã được sử dụng");
            }
        }

        if (staffProfile.getNumberPhone() == null || staffProfile.getNumberPhone().isEmpty()) {
            bindingResult.rejectValue("numberPhone", "error.staffProfile", "Số điện thoại không được để trống");
        } else if (!staffProfile.getNumberPhone().matches("^(0|\\+84)(\\d{9})$")) {
            bindingResult.rejectValue("numberPhone", "error.staffProfile", "Số điện thoại không hợp lệ");
        }

        if (staffProfile.getAddRessDetail() == null || staffProfile.getAddRessDetail().trim().isEmpty()) {
            bindingResult.rejectValue("addRessDetail", "error.staffProfile", "Địa chỉ cụ thể không được để trống!");
        } else if (staffProfile.getAddRessDetail().length() > 260) {
            bindingResult.rejectValue("addRessDetail", "error.staffProfile", "Địa chỉ cụ thể không được vượt quá 260 ký tự!");
        }

        if(bindingResult.hasErrors()){
            model.addAttribute("staffProfile", staffProfile);

            return "Profile/staff_profile";
        }
        if (staff != null) {
            staff.setEmail(staffProfile.getEmail());
            staff.setFullName(staffProfile.getFullName());
            staff.setNumberPhone(staffProfile.getNumberPhone());
            staff.setGender(staffProfile.getGender());
            staff.setBirthDay(staffProfile.getBirthDay());
            staff.setAddress(staffProfile.getWard() + "," + staffProfile.getDistrict() + "," + staffProfile.getProvince() + "," + staffProfile.getAddRessDetail());
            staffService.save(staff);

            if (!nameImage.isEmpty()) {
                String imageId = staffService.uploadFile(nameImage, staff.getId());
                String randomImageName = extractRandomImageName(imageId);
                staff.setImage(randomImageName);
            } else {
                System.out.println("Không có ảnh mới, giữ nguyên ảnh cũ: " + staff.getImage());
            }

            model.addAttribute("staffProfile", staffProfile);
            model.addAttribute("successMessage", "Cập nhật thông tin thành công.");
            mess ="Cập nhật thông tin thành công";
            check ="1";
        } else {
            model.addAttribute("errorMessage", "Không tìm thấy thông tin tài khoản nhân viên.");
        }

        return "redirect:/profile/staffProfile";
    }

    private String extractRandomImageName(String url) {

        String[] parts = url.split("/");
        String fileNameWithExtension = parts[parts.length - 1];
        String fileName = fileNameWithExtension.split("\\.")[0];

        return fileName;
    }

    @PostMapping("/updatePasswordStaff")
    public String updatePassword(@Valid @ModelAttribute("staffProfile") StaffProfileRequest staffProfile,
                                 BindingResult bindingResult,
                                 HttpSession session,
                                 Model model) {
        Staff staff = (Staff) session.getAttribute("staffLogin");

        if (staff == null) {
            return "redirect:/login";
        }
        if(staff.getStatus() != 1) {
            return "redirect:/home_manage";
        }

        if (staffProfile.getCurrentPassword() == null || staffProfile.getCurrentPassword().isEmpty()) {
            bindingResult.rejectValue("currentPassword", "error.userProfile", "Mật khẩu hiện tại không được để trống");
        } else if (!staffProfile.getCurrentPassword().equals(staff.getPassword())) {
            bindingResult.rejectValue("currentPassword", "error.userProfile", "Mật khẩu hiện tại không đúng");
        }

        if (staffProfile.getNewPassword() == null || staffProfile.getNewPassword().isEmpty()) {
            bindingResult.rejectValue("newPassword", "error.userProfile", "Mật khẩu mới không được để trống");
        } else if (staffProfile.getNewPassword().length() > 250) {
            bindingResult.rejectValue("currentPassword", "error.userProfile", "Mật khẩu hiện tại không lớn hơn 250 ký tự");
        }

        if (staffProfile.getConfirmPassword() == null || staffProfile.getConfirmPassword().isEmpty()) {
            bindingResult.rejectValue("confirmPassword", "error.userProfile", "Mật khẩu xác nhận không được để trống");
        } else if (!staffProfile.getNewPassword().equals(staffProfile.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.userProfile", "Mật khẩu xác nhận không khớp");
        } else if (staffProfile.getNewPassword().length() > 250) {
            bindingResult.rejectValue("confirmPassword", "error.userProfile", "Mật khẩu xác nhận không lớn hơn 250 ký tự");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("staffProfile", staffProfile);
            model.addAttribute("showPasswordForm", true);
            return "Profile/staff_profile";
        }


        staff.setPassword(staffProfile.getNewPassword());
        staffService.save(staff);

        model.addAttribute("successMessage", "Đã cập nhật mật khẩu thành công.");
        mess ="Cập nhật mật khẩu thành công";
        check ="1";
        return "redirect:/profile/staffProfile";
    }

    @ModelAttribute("staffProfile")
    public StaffProfileRequest prepareStaffProfile(HttpSession session,
                                                   Model model) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        StaffProfileRequest staffProfile = new StaffProfileRequest();

        if (staff != null) {

            staffProfile.setStatus(staff.getStatus());
            staffProfile.setFullName(staff.getFullName());
            staffProfile.setEmail(staff.getEmail());
            staffProfile.setNumberPhone(staff.getNumberPhone());
            staffProfile.setGender(staff.getGender());
            staffProfile.setBirthDay(staff.getBirthDay());
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

            staffProfile.setImageStaffString(staff.getImage());

            String[] partStaff = staff.getAddress().split(",\\s*");
            staffProfile.setProvince(partStaff[2]);
            staffProfile.setDistrict(partStaff[1]);
            staffProfile.setWard(partStaff[0]);
            staffProfile.setAddRessDetail(String.join(", ", java.util.Arrays.copyOfRange(partStaff, 3, partStaff.length)));
        }
        return staffProfile;
    }
    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session){
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }
}
