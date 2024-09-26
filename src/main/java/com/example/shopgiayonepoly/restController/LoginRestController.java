package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.dto.request.loginRequest;
import com.example.shopgiayonepoly.dto.response.loginReponse;
import com.example.shopgiayonepoly.entites.Staff;

import com.example.shopgiayonepoly.repositores.StaffSecurityRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.ok("Login thành công");
        } else {
            // Đăng nhập thất bại
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login thất bại");
        }
    }

    @GetMapping("/check-login")
    public ResponseEntity<String> checkLoginStatus(HttpSession session) {
        Staff staff = (Staff) session.getAttribute("loggedInUser");

        if (staff != null) {
            return ResponseEntity.ok("Đăng nhập thành công với: " + staff.getFullName() + " - Vai trò: " + (staff.getRole() != null ? staff.getRole().getNameRole() : "No role"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Đăng nhập thất bại");
        }
    }
}