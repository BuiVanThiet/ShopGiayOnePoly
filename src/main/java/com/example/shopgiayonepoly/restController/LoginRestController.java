package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.dto.request.RegisterRequest;
import com.example.shopgiayonepoly.dto.request.loginRequest;
import com.example.shopgiayonepoly.dto.response.loginReponse;
import com.example.shopgiayonepoly.entites.Role;
import com.example.shopgiayonepoly.entites.Staff;

import com.example.shopgiayonepoly.repositores.StaffSecurityRepository;
import com.example.shopgiayonepoly.repositores.roleReponsitory;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login-api")
public class LoginRestController {
    @Autowired
    private StaffSecurityRepository staffRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody loginRequest loginRequest, HttpSession session) {
        Staff staff = staffRepository.findByAcountOrEmail(loginRequest.getAccount(), loginRequest.getAccount());
        if (staff != null && loginRequest.getPassword().equals(staff.getPassword())) {
            // Đăng nhập thành công, lưu thông tin vào session
            session.setAttribute("loggedInUser", staff);
            System.out.println("Session ID: " + session.getId());

            loginReponse response = new loginReponse(staff.getFullName(), staff.getRole() != null ? staff.getRole().getNameRole() : "No role");
            return ResponseEntity.ok(response);
        } else {
            // Đăng nhập thất bại
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login thất bại");
        }
    }   

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(HttpSession session) {
        Staff loggedInUser = (Staff) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            loginReponse response = new loginReponse(loggedInUser.getFullName(), loggedInUser.getRole().getNameRole());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
        }
    }


}

