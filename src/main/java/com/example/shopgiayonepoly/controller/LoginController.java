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
    import org.springframework.ui.ModelMap;
    import org.springframework.web.bind.annotation.*;

    import java.security.Principal;

    @Controller
    public class LoginController {
        @Autowired
        StaffSecurityRepository staffSecurityRepository;

        @GetMapping("/login")
        public String login() {
            return "login/login";
        }
        @GetMapping("/home_manage")
        public String homeManage(Model model, HttpSession session) {
            Staff staff = (Staff) session.getAttribute("staffLogin");
            if (staff != null) {
                    model.addAttribute("staffInfo", staff);
            } else {
                    session.invalidate();
                    return "redirect:/login";

            }
            return "Home/home_manege";
        }
        @GetMapping("/logout")
        public String logout(HttpSession session) {
            // Xóa thông tin khỏi session khi đăng xuất
            session.invalidate();
            return "redirect:/login";
        }

//        @ModelAttribute("staffInfo")
//        public Staff staff(HttpSession session) {
//            Staff staff = (Staff) session.getAttribute("loggedInUser");
//            return staff;
//        }

    }


