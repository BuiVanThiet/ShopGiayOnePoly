//    package com.example.shopgiayonepoly.controller;
//
//    import com.example.shopgiayonepoly.dto.request.loginRequest;
//    import com.example.shopgiayonepoly.entites.Staff;
//    import com.example.shopgiayonepoly.repositores.StaffSecurityRepository;
//    import jakarta.servlet.http.HttpServletRequest;
//    import jakarta.servlet.http.HttpSession;
//    import org.springframework.beans.factory.annotation.Autowired;
//    import org.springframework.security.core.Authentication;
//    import org.springframework.security.core.context.SecurityContextHolder;
//    import org.springframework.security.core.userdetails.UserDetails;
//    import org.springframework.stereotype.Controller;
//    import org.springframework.ui.Model;
//    import org.springframework.ui.ModelMap;
//    import org.springframework.web.bind.annotation.GetMapping;
//    import org.springframework.web.bind.annotation.PostMapping;
//    import org.springframework.web.bind.annotation.RequestMapping;
//    import org.springframework.web.bind.annotation.RequestParam;
//
//    import java.security.Principal;
//
//    @Controller
//    public class LoginController {
//
//        @Autowired
//        StaffSecurityRepository staffSecurityRepository;
//
//        @GetMapping("/login")
//        public String showLoginForm() {
//            return "login/login";
//        }
//        @PostMapping("/login")
//        public String login(HttpServletRequest request, ModelMap model) {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//                Staff staff = staffSecurityRepository.findByAcountOrEmail(userDetails.getUsername(), userDetails.getUsername());
//                if (staff != null) {
//                    HttpSession session = request.getSession();
//                    session.setAttribute("loggedInUser", staff); // Lưu thông tin vào session
//                    return "redirect:/info"; // Chuyển hướng đến trang thông tin
//                }
//            }
//            model.addAttribute("message", "Đăng nhập không thành công");
//            return "Login/login"; // Trả về trang đăng nhập nếu không thành công
//        }
//
//        @GetMapping("/info")
//        public String getHomePage(ModelMap modelMap, HttpSession session) {
//            // Lấy thông tin người dùng từ session
//            Staff loggedInUser = (Staff) session.getAttribute("loggedInUser");
//            if (loggedInUser != null) {
//                modelMap.addAttribute("username", loggedInUser.getFullName());
//                modelMap.addAttribute("nameRole", loggedInUser.getRole() != null ? loggedInUser.getRole().getNameRole() : "No role");
//            } else {
//                modelMap.addAttribute("message", "Chưa có người dùng đăng nhập.");
//            }
//            return "Home/home_manege";
//        }
//
//
//
//        @GetMapping("/logout")
//        public String logout(HttpSession session) {
//            // Xóa thông tin khỏi session khi đăng xuất
//            session.invalidate();
//            return "redirect:/login";
//        }
//    }
