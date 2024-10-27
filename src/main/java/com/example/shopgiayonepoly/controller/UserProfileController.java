package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.UserProfileUpdateRequest;
import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.repositores.CustomerRegisterRepository;
import com.example.shopgiayonepoly.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
        if (clientLoginResponse != null) {
            String acount = clientLoginResponse.getAcount();

            Customer customer = customerRegisterRepository.findByAcount(acount);
            if (customer != null) {
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
                if (birthDay != null) {
                    model.addAttribute("birthDayDay", birthDay.getDayOfMonth());
                    model.addAttribute("birthDayMonth", birthDay.getMonthValue());
                    model.addAttribute("birthDayYear", birthDay.getYear());
                } else {
                    model.addAttribute("birthDayDay", "");
                    model.addAttribute("birthDayMonth", "");
                    model.addAttribute("birthDayYear", "");
                }

                model.addAttribute("userProfile", userProfile);
                model.addAttribute("clientLogin", clientLoginResponse);
                return "Profile/UserProfile";
            } else {
                model.addAttribute("errorMessage", "Không tìm thấy thông tin tài khoản.");
            }
        } else {
            return "login/loginClient"; // Chuyển hướng đến trang đăng nhập nếu người dùng chưa đăng nhập
        }

        return "Profile/UserProfile"; // Trả về view với thông báo lỗi
    }

    @PostMapping("/userProfileUpdate")
    public String updateProfile(UserProfileUpdateRequest userProfile,
                                HttpSession session, @RequestParam("nameImage") MultipartFile nameImage, Model model) throws IOException {
        ClientLoginResponse clientLoginResponse = (ClientLoginResponse) session.getAttribute("clientLogin");
        if (clientLoginResponse != null) {
            String acount = clientLoginResponse.getAcount();
            Customer customer = customerRegisterRepository.findByAcount(acount);
            if (customer != null) {
                customer.setFullName(userProfile.getFullName());
                customer.setPassword(userProfile.getPassword());
                customer.setEmail(userProfile.getEmail());
                customer.setNumberPhone(userProfile.getNumberPhone());
                customer.setGender(userProfile.getGender());
                customer.setBirthDay(userProfile.getBirthDay());
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
        } else {
            return "login/loginClient"; // Chuyển hướng đến trang đăng nhập nếu người dùng chưa đăng nhập
        }

        return "redirect:/profile/userProfile"; // Trả về view với thông báo lỗi
    }

    @ModelAttribute("clientLogin")
    public ClientLoginResponse populateClientLogin(HttpSession session) {
        return (ClientLoginResponse) session.getAttribute("clientLogin");
    }
}
