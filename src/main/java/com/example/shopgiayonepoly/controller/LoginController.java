//package com.example.shopgiayonepoly.controller;
//
//import com.example.shopgiayonepoly.dto.request.loginRequest;
//import com.example.shopgiayonepoly.entites.Staff;
//import com.example.shopgiayonepoly.repositores.StaffSecurityRepository;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.security.Principal;
//
//@Controller
//public class LoginController {
//
//    @Autowired
//    StaffSecurityRepository staffSecurityRepository;
//
//    @GetMapping("/login")
//    public String showLoginForm() {
//        return "login/login";
//    }
//    @GetMapping("/home_manage")
//    public String homeManage(Model model, Principal principal) {
//        if (principal != null) {
//            String username = principal.getName(); // Tài khoản hoặc email
//            Staff staff = staffSecurityRepository.findByAcountOrEmail(username, username);
//
//            if (staff != null) {
//                model.addAttribute("fullName", staff.getFullName());
//                model.addAttribute("roleName", staff.getRole() != null ? staff.getRole().getNameRole() : "Không có vai trò");
//            } else {
//                model.addAttribute("fullName", "Lỗi");
//                model.addAttribute("roleName", "Không có vai trò");
//            }
//        } else {
//            model.addAttribute("fullName", "Lỗi");
//            model.addAttribute("roleName", "Không có vai trò");
//        }
//        return "Home/home_manege";
//    }
//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        // Xóa thông tin khỏi session khi đăng xuất
//        session.invalidate();
//        return "redirect:/login";
//    }
//}
