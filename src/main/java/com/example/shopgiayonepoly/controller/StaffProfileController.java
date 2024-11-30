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
            // Bạn có thể sử dụng thông tin staff để thực hiện các thao tác khác
            StaffProfileRequest staffProfile = new StaffProfileRequest();
//            staffProfile.setAccount(staff.getAcount());
            staffProfile.setFullName(staff.getFullName());
//            staffProfile.setPassword(staff.getPassword());
            staffProfile.setEmail(staff.getEmail());
            staffProfile.setNumberPhone(staff.getNumberPhone());
            staffProfile.setGender(staff.getGender());
            staffProfile.setBirthDay(staff.getBirthDay());
            staffProfile.setImageStaffString(staff.getImage()); // Đặt giá trị mặc định

            staffProfile.setStatus(staff.getStatus());

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
            model.addAttribute("check",check);
            model.addAttribute("mess",mess);
            model.addAttribute("staffProfile", staffProfile);
            mess ="";
            check ="";
        } else {
            model.addAttribute("errorMessage", "Không tìm thấy thông tin tài khoản nhân viên.");
            return "login/login"; // Chuyển hướng về trang đăng nhập nếu chưa có thông tin
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
            return "redirect:/login"; // Chuyển hướng về trang đăng nhập nếu không tìm thấy nhân viên
        }

        if(staff.getStatus() != 1) {
            return "redirect:/home_manage";
        }

        Map<String,String> checkLoginAndLogout = checkLoginAndLogOutByStaff(staff.getId());
        String messMap = checkLoginAndLogout.get("message");
        if(!messMap.trim().equals("")) {
            mess = (messMap);
            check = "3";
            return "redirect:/profile/staffProfile";
        }
        System.out.println("Staff từ model: " + staff);
        staffProfile.setImageStaffString(staff.getImage());

        // Kiểm tra hợp lệ cho fullName
        if (staffProfile.getFullName() == null || staffProfile.getFullName().trim().isEmpty()) {
            bindingResult.rejectValue("fullName", "error.staffProfile", "Họ và tên không được để trống");

        } else if (!staffProfile.getFullName().matches("^[\\p{L}\\s]+$")) {
            bindingResult.rejectValue("fullName", "error.staffProfile", "Họ và tên chỉ được nhập chữ cái");

        } else if (staffProfile.getFullName().length() < 5 || staffProfile.getFullName().length() > 255) {
            bindingResult.rejectValue("fullName", "error.staffProfile", "Họ và tên phải có độ dài từ 5 đến 255 ký tự");

        }

        // Kiểm tra hợp lệ cho email
        if (staffProfile.getEmail() == null || staffProfile.getEmail().isEmpty()) {
            bindingResult.rejectValue("email", "error.staffProfile", "Email không được để trống");
        } else if (staffProfile.getEmail().length() >100) {
            bindingResult.rejectValue("email", "error.staffProfile", "Email không được lớn hơn 100 ký tự");
        } else if (!staffProfile.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            bindingResult.rejectValue("email", "error.staffProfile", "Email không hợp lệ");
        } else if (staffService.existsByEmail(staffProfile.getEmail()) !=null || customerService.existsByEmail(staffProfile.getEmail()) !=null) {
            bindingResult.rejectValue("email", "error.staffProfile", "Email đã tồn tại");
        }

        // Kiểm tra hợp lệ cho số điện thoại
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
            System.out.println("loi ne");
            model.addAttribute("staffProfile", staffProfile);
            System.out.println(staffProfile);

            return "Profile/staff_profile";
        }
        if (staff != null) {
            staff.setEmail(staffProfile.getEmail());
            staff.setFullName(staffProfile.getFullName());
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
                System.out.println("Tên ảnh: " + randomImageName);
            } else {
                // Nếu không có ảnh mới, giữ nguyên ảnh cũ
                System.out.println("Không có ảnh mới, giữ nguyên ảnh cũ: " + staff.getImage());
            }

            model.addAttribute("staffProfile", staffProfile);
            model.addAttribute("successMessage", "Cập nhật thông tin thành công.");
            mess ="Cập nhật thông tin thành công";
            check ="1";
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

    @PostMapping("/updatePasswordStaff")
    public String updatePassword(@Valid @ModelAttribute("staffProfile") StaffProfileRequest staffProfile,
                                 BindingResult bindingResult,
                                 HttpSession session,
                                 Model model) {
        Staff staff = (Staff) session.getAttribute("staffLogin");

        if (staff == null) {
            return "redirect:/login"; // Chuyển hướng về trang đăng nhập nếu không tìm thấy nhân viên
        }
        if(staff.getStatus() != 1) {
            return "redirect:/home_manage";
        }
        // Kiểm tra mật khẩu hiện tại
        if (staffProfile.getCurrentPassword() == null || staffProfile.getCurrentPassword().isEmpty()) {
            bindingResult.rejectValue("currentPassword", "error.userProfile", "Mật khẩu hiện tại không được để trống");
        } else if (!staffProfile.getCurrentPassword().equals(staff.getPassword())) {
            bindingResult.rejectValue("currentPassword", "error.userProfile", "Mật khẩu hiện tại không đúng");
        }

        // Kiểm tra mật khẩu mới và xác nhận
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
            return "Profile/staff_profile"; // Quay lại trang với các lỗi validation
        }

        // Cập nhật mật khẩu mới
        staff.setPassword(staffProfile.getNewPassword());
        staffService.save(staff); // Lưu thông tin nhân viên sau khi cập nhật

        model.addAttribute("successMessage", "Đã cập nhật mật khẩu thành công.");
        mess ="Cập nhật mật khẩu thành công";
        check ="1";
        return "redirect:/profile/staffProfile"; // Chuyển hướng về trang hồ sơ sau khi cập nhật
    }

    @ModelAttribute("staffProfile")
    public StaffProfileRequest prepareStaffProfile(HttpSession session,
                                                   Model model) {
        Staff staff = (Staff) session.getAttribute("staffLogin");
        StaffProfileRequest staffProfile = new StaffProfileRequest();

        if (staff != null) {
            // Gán giá trị từ staff cho staffProfile trước khi ràng buộc
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
            // Gán các trường khác cần thiết từ staff vào staffProfile
        }
        return staffProfile;
    }
    protected Map<String,String> checkLoginAndLogOutByStaff(Integer idStaff) {
        Map<String,String> thongBao = new HashMap<>();
        String checkLogin = getCheckStaffAttendanceYetBill(idStaff,1);
        String checkLogOut = getCheckStaffAttendanceYetBill(idStaff,2);
        System.out.println(checkLogin);
        if(!checkLogin.equals("Có")) {
            thongBao.put("message","Mời bạn điểm danh trước khi làm việc!");
            return thongBao;
        }

        if(checkLogin.equals("Có") && checkLogOut.equals("Có")) {
            thongBao.put("message","Bạn đã điểm danh vào và ra rồi, không thể làm việc được nữa!");
            return thongBao;
        }
        thongBao.put("message","");
        return thongBao;
    }

    protected String getCheckStaffCashierInventory(
            Integer idStaff
    ) {
        List<Object[]> checkCashierInventory = this.cashierInventoryService.getCheckCashierInventoryStaff(idStaff);

        // Kiểm tra nếu danh sách không rỗng và có kết quả
        if (!checkCashierInventory.isEmpty() && checkCashierInventory.get(0).length > 0) {
            // Lấy giá trị đầu tiên từ kết quả
            return checkCashierInventory.get(0)[0].toString();
        }
        // Trường hợp không có dữ liệu
        return "Không";
    }
    protected String getCheckStaffAttendanceYetBill(
//            @PathVariable("id") Integer idStaff,@PathVariable("type") Integer timekeepingTypeCheck
            Integer idStaff, Integer timekeepingTypeCheck
    ) {
        List<Object[]> checkLoginLogOut = this.timekeepingService.getCheckStaffAttendanceYet(idStaff, timekeepingTypeCheck);

        // Kiểm tra nếu danh sách không rỗng và có kết quả
        if (!checkLoginLogOut.isEmpty() && checkLoginLogOut.get(0).length > 0) {
            // Lấy giá trị đầu tiên từ kết quả
            return checkLoginLogOut.get(0)[0].toString();
        }
        // Trường hợp không có dữ liệu
        return "Không";
    }
    @ModelAttribute("staffInfo")
    public Staff staff(HttpSession session){
        Staff staff = (Staff) session.getAttribute("staffLogin");
        return staff;
    }

}
