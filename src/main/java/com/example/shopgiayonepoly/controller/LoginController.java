package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.loginRequest;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.repositores.StaffSecurityRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class LoginController {

    @Autowired
    StaffSecurityRepository staffSecurityRepository;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        Staff staff = staffSecurityRepository.findByAcountOrEmail(username, username);

        if (staff != null && password.equals(staff.getPassword())) {
            // Đăng nhập thành công, lưu thông tin vào session
            session.setAttribute("loggedInUser", staff);

            // Thêm thông tin vào mô hình để hiển thị trên trang
            model.addAttribute("fullName", staff.getFullName());
            model.addAttribute("roleName", staff.getRole() != null ? staff.getRole().getNameRole() : "No role");

            return "Home/home_manage"; // Chuyển hướng đến trang home/manage.html
        } else {
            // Đăng nhập thất bại, quay lại trang đăng nhập với thông báo lỗi
            model.addAttribute("error", "Tài khoản hoặc mật khẩu không đúng");
            return "login"; // Quay lại trang đăng nhập
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Xóa thông tin khỏi session khi đăng xuất
        session.invalidate();
        return "redirect:/login";
    }
}
