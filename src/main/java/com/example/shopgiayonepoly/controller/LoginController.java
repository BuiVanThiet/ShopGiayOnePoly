package com.example.shopgiayonepoly.controller;

import com.example.shopgiayonepoly.dto.request.loginRequest;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.repositores.StaffSecurityRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    @GetMapping("/home_manage")
    public String homePage(HttpSession session) {
        // Lấy dữ liệu từ session đã lưu sau khi đăng nhập thành công
        String fullName = (String) session.getAttribute("fullName");
        String roleName = (String) session.getAttribute("roleName");

        // Kiểm tra nếu thông tin không tồn tại trong session thì có thể xử lý phù hợp, ví dụ chuyển hướng về trang đăng nhập
        if (fullName == null || roleName == null) {
            return "redirect:/login"; // Chuyển hướng về trang login nếu không có thông tin trong session
        }

        // Hiển thị trang home manage
        return "Home/home_manege"; // Đảm bảo tên template đúng
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Xóa thông tin khỏi session khi đăng xuất
        session.invalidate();
        return "redirect:/login";
    }
}
