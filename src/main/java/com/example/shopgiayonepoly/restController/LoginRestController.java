//package com.example.shopgiayonepoly.restController;
//import com.example.shopgiayonepoly.dto.request.loginRequest;
//import com.example.shopgiayonepoly.entites.Customer;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api")
//public class LoginRestController {
////    @Autowired
////    private AuthenticationManager authenticationManager;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody loginRequest loginRequest, HttpSession session) {
//        try {
//            // Xác thực người dùng
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
//            );
//
//            // Lưu đối tượng xác thực vào SecurityContextHolder
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            // Lưu thông tin người dùng vào session
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            session.setAttribute("user", userDetails); // Lưu thông tin người dùng vào session
//
//            return ResponseEntity.ok("Đăng nhập thành công");
//        } catch (Exception e) {
//            return ResponseEntity.status(401).body("Đăng nhập thất bại");
//        }
//    }
//
//    @GetMapping("/checkLoginStatus")
//    public ResponseEntity<?> checkLoginStatus(HttpSession session) {
//        UserDetails user = (UserDetails) session.getAttribute("user");
//        if (user != null) {
//            return ResponseEntity.ok(user); // Trả về thông tin người dùng
//        } else {
//            return ResponseEntity.status(401).body("Chưa đăng nhập");
//        }
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpSession session) {
//        session.invalidate(); // Xóa session
//        SecurityContextHolder.clearContext(); // Xóa SecurityContext
//        return ResponseEntity.ok("Đăng xuất thành công");
//    }
//}
