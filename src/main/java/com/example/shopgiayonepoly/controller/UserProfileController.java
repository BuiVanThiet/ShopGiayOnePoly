package com.example.shopgiayonepoly.controller;

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
        userProfile.setPassword(customer.getPassword());
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
            customer.setFullName(userProfile.getFullName());
            customer.setPassword(userProfile.getPassword());
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

    @ModelAttribute("userProfile")
    public UserProfileUpdateRequest populateUserProfile(HttpSession session) {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        if (clientLoginResponse != null) {
            String account = clientLoginResponse.getAcount();
            Customer customer = customerRegisterRepository.findByAcount(account);
            if (customer != null) {
                UserProfileUpdateRequest userProfile = new UserProfileUpdateRequest();
                userProfile.setPassword(customer.getPassword());
                userProfile.setFullName(customer.getFullName());
                userProfile.setEmail(customer.getEmail());
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
