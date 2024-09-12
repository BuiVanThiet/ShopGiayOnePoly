//package com.example.shopgiayonepoly.restController;
//
//import com.example.shopgiayonepoly.dto.request.loginRequest;
//import com.example.shopgiayonepoly.entites.Staff;
//
//import com.example.shopgiayonepoly.repositores.StaffSecurityRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/login-api")
//public class LoginRestController {
//    @Autowired
//    private StaffSecurityRepository staffRepository;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody loginRequest loginRequest) {
//        Staff staff = staffRepository.findByAcountOrEmail(loginRequest.getAcount(), loginRequest.getAcount());
//
//        if (staff != null && loginRequest.getPassword().equals(staff.getPassword())) {
//            // Đăng nhập thành công
//            return ResponseEntity.ok("Login thành công");
//        } else {
//            // Đăng nhập thất bại
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login thất bại");
//        }
//    }
//
//    @GetMapping("/status")
//    public String checkStatus() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
//            return "User is logged in as " + authentication.getName();
//        } else {
//            return "User is not logged in";
//        }
//    }
//}