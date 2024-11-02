package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.config.PasswordEncoderConfig;
import com.example.shopgiayonepoly.dto.request.StaffProfileRequest;
import com.example.shopgiayonepoly.dto.request.UserProfileUpdateRequest;
import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.repositores.CustomerRegisterRepository;
import com.example.shopgiayonepoly.service.CustomerService;
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
public class UserProfileController {
    @Autowired
    CustomerRegisterRepository customerRegisterRepository;
    @Autowired
    CustomerService customerService;

    @GetMapping("/userProfile")
    public String formProfile(Model model, HttpSession session) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        System.out.println("ben profile clinet: " + clientLoginResponse.toString());
        if (clientLoginResponse == null) {
            return "login/loginClient"; // Chuyển hướng đến trang đăng nhập nếu người dùng chưa đăng nhập
        }

        String account = clientLoginResponse.getAcount();
        Customer customer = customerRegisterRepository.findByAcount(account);

        if (customer == null) {
            model.addAttribute("errorMessage", "Không tìm thấy thông tin tài khoản.");
            return "login/loginClient";
        }

        UserProfileUpdateRequest userProfile = new UserProfileUpdateRequest();
        userProfile.setAccount(customer.getAcount());
        userProfile.setFullName(customer.getFullName());
        userProfile.setEmail(customer.getEmail());
        userProfile.setNumberPhone(customer.getNumberPhone());
        userProfile.setGender(customer.getGender());
        userProfile.setBirthDay(customer.getBirthDay());
        userProfile.setImageString(customer.getImage());

        String[] part = customer.getAddRess().split(",\\s*");
        userProfile.setProvince(part[2]);
        userProfile.setDistrict(part[1]);
        userProfile.setWard(part[0]);
        userProfile.setAddRessDetail(String.join(", ", java.util.Arrays.copyOfRange(part, 3, part.length)));

        LocalDate birthDay = customer.getBirthDay();
        model.addAttribute("birthDayDay", birthDay != null ? birthDay.getDayOfMonth() : null);
        model.addAttribute("birthDayMonth", birthDay != null ? birthDay.getMonthValue() : null);
        model.addAttribute("birthDayYear", birthDay != null ? birthDay.getYear() : null);

        model.addAttribute("userProfile", userProfile);
        model.addAttribute("clientLogin", clientLoginResponse);

        return "Profile/UserProfile";
    }

    @PostMapping("/userProfileUpdate")
    public String updateProfile(@Valid @ModelAttribute("userProfile") UserProfileUpdateRequest userProfile,
                                BindingResult bindingResult,
                                HttpSession session,
                                @RequestParam("nameImage") MultipartFile nameImage,
                                @RequestParam("dob-day") int day,
                                @RequestParam("dob-month") int month,
                                @RequestParam("dob-year") int year,
                                Model model) throws IOException {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");

        if (clientLoginResponse == null) {
            return "login/loginClient";
        }

        String acount = clientLoginResponse.getAcount();
        Customer customer = customerRegisterRepository.findByAcount(acount);

        // Kiểm tra hợp lệ cho fullName
        if (userProfile.getFullName() == null || userProfile.getFullName().trim().isEmpty()) {
            bindingResult.rejectValue("fullName", "error.userProfile", "Họ và tên không được để trống");

        } else if (!userProfile.getFullName().matches("^[\\p{L}\\s]+$")) {
            bindingResult.rejectValue("fullName", "error.userProfile", "Họ và tên chỉ được nhập chữ cái");

        } else if (userProfile.getFullName().length() < 5 || userProfile.getFullName().length() > 100) {
            bindingResult.rejectValue("fullName", "error.userProfile", "Họ và tên phải có độ dài từ 5 đến 100 ký tự");

        }

        // Kiểm tra hợp lệ cho email
        if (userProfile.getEmail() == null || userProfile.getEmail().isEmpty()) {
            bindingResult.rejectValue("email", "error.userProfile", "Email không được để trống");
        } else if (!userProfile.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            bindingResult.rejectValue("email", "error.userProfile", "Email không hợp lệ");
        }

        // Kiểm tra hợp lệ cho số điện thoại
        if (userProfile.getNumberPhone() == null || userProfile.getNumberPhone().isEmpty()) {
            bindingResult.rejectValue("numberPhone", "error.userProfile", "Số điện thoại không được để trống");
        } else if (!userProfile.getNumberPhone().matches("^(0|\\+84)(\\d{9})$")) {
            bindingResult.rejectValue("numberPhone", "error.userProfile", "Số điện thoại không hợp lệ");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("userProfile", userProfile);
            model.addAttribute("clientLogin", clientLoginResponse);
            model.addAttribute("birthDayDay", day);
            model.addAttribute("birthDayMonth", month);
            model.addAttribute("birthDayYear", year);
            return "Profile/UserProfile";// Quay lại trang với các lỗi validation
        }
        if (customer != null) {
            // Cập nhật thông tin người dùng
            customer.setAcount(userProfile.getAccount());
            customer.setFullName(userProfile.getFullName());
            customer.setEmail(userProfile.getEmail());
            customer.setNumberPhone(userProfile.getNumberPhone());
            customer.setGender(userProfile.getGender());

            // Cập nhật ngày sinh từ các giá trị `day`, `month`, `year`
            LocalDate birthDay = LocalDate.of(year, month, day);
            customer.setBirthDay(birthDay);

            customer.setAddRess(userProfile.getWard() + "," + userProfile.getDistrict() + "," + userProfile.getProvince() + "," + userProfile.getAddRessDetail());

            if (!nameImage.isEmpty()) {
                customer.setImage(nameImage.getOriginalFilename());
                customerService.uploadFile(nameImage, customer.getId());
            }

            customerRegisterRepository.save(customer);

            model.addAttribute("clientLogin", clientLoginResponse);
            model.addAttribute("userProfile", userProfile);
            model.addAttribute("successMessage", "Cập nhật thông tin thành công.");
        } else {
            model.addAttribute("errorMessage", "Không tìm thấy thông tin tài khoản.");
        }

        return "redirect:/profile/userProfile";
    }
    @PostMapping("/updatePassword")
    public String updatePassword(@Valid @ModelAttribute("userProfile") UserProfileUpdateRequest userProfile,
                                 BindingResult bindingResult,
                                 HttpSession session,
                                 Model model) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");

        if (clientLoginResponse == null) {
            return "login/loginClient";
        }

        String acount = clientLoginResponse.getAcount();
        Customer customer = customerRegisterRepository.findByAcount(acount);

        if (customer != null) {
            if (!userProfile.getCurrentPassword().equals(customer.getPassword())) {
                bindingResult.rejectValue("currentPassword", "error.userProfile", "Mật khẩu hiện tại không đúng");
            }

            // Kiểm tra mật khẩu mới và xác nhận
            if (!userProfile.getNewPassword().equals(userProfile.getConfirmPassword())) {
                bindingResult.rejectValue("confirmPassword", "error.userProfile", "Mật khẩu xác nhận không khớp");
            }

            if (bindingResult.hasErrors()) {
                model.addAttribute("clientLogin", clientLoginResponse);
                model.addAttribute("userProfile", userProfile);
                model.addAttribute("showPasswordForm", true);
                return "Profile/UserProfile"; // Quay lại trang với các lỗi validation
            }

            // Cập nhật mật khẩu
            customer.setPassword(userProfile.getNewPassword()); // Lưu mật khẩu mới
            customerRegisterRepository.save(customer);

            model.addAttribute("clientLogin", clientLoginResponse);
            model.addAttribute("userProfile", userProfile);
            model.addAttribute("successMessage", "Cập nhật mật khẩu thành công.");
        } else {
            model.addAttribute("errorMessage", "Không tìm thấy thông tin tài khoản.");
        }

        return "redirect:/profile/userProfile"; // Chuyển hướng về trang thông tin tài khoản
    }

    @ModelAttribute("userProfile")
    public UserProfileUpdateRequest populateUserProfile(HttpSession session) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        if (clientLoginResponse != null) {
            String account = clientLoginResponse.getAcount();
            Customer customer = customerRegisterRepository.findByAcount(account);
            if (customer != null) {
                UserProfileUpdateRequest userProfile = new UserProfileUpdateRequest();
                userProfile.setFullName(customer.getFullName());
                userProfile.setEmail(customer.getEmail());
                userProfile.setCurrentPassword(customer.getPassword());
                userProfile.setNumberPhone(customer.getNumberPhone());
                userProfile.setGender(customer.getGender());
                userProfile.setImageString(customer.getImage());
                userProfile.setStatus(customer.getStatus()); // Thiết lập status từ customer

                return userProfile; // Trả về userProfile
            }
        }
        return new UserProfileUpdateRequest(); // Trả về đối tượng rỗng nếu không tìm thấy
    }
    @ModelAttribute("clientLogin")
    public ClientLoginResponse populateClientLogin(HttpSession session) {
        return (ClientLoginResponse) session.getAttribute("clientLogin");
    }
}
