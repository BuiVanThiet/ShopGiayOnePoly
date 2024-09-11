//package com.example.shopgiayonepoly.restController;
//
//import com.example.shopgiayonepoly.dto.request.loginRequest;
//import com.example.shopgiayonepoly.entites.Staff;
//
//import com.example.shopgiayonepoly.repositores.StaffSecurityRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/login-api")
//public class LoginRestController {
//    @Autowired
//    private StaffSecurityRepository staffRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody loginRequest loginRequest) {
//        Staff staff = staffRepository.findByAcountOrEmail(loginRequest.getAcount(), loginRequest.getAcount());
//
//        if (staff != null && passwordEncoder.matches(loginRequest.getPassword(), staff.getPassword())) {
//            // Đăng nhập thành công
//            return ResponseEntity.ok("Login successful");
//        } else {
//            // Đăng nhập thất bại
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//        }
//    }
//
//}
