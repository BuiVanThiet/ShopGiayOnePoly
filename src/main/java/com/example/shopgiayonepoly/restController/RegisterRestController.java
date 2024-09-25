package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.dto.request.RegisterRequest;
import com.example.shopgiayonepoly.entites.Role;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.repositores.StaffSecurityRepository;
import com.example.shopgiayonepoly.repositores.roleReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/register-api")
public class RegisterRestController {
    @Autowired
    private StaffSecurityRepository staffRepository;
    @Autowired
    private roleReponsitory roleReponsitory;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Mật khẩu và xác nhận mật khẩu không khớp!"));
        }

        if(staffRepository.findByAcountOrEmail(registerRequest.getAcount(), registerRequest.getEmail()) != null){
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Tài khoản hoặc email đã tồn tại!"));
        }

        // Thêm mới nhân viên
        Staff staff = new Staff();
        staff.setFullName(registerRequest.getFullName());
        staff.setGender(registerRequest.getGender());
        staff.setEmail(registerRequest.getEmail());
        staff.setNumberPhone(registerRequest.getNumberPhone());
        staff.setAddress(registerRequest.getAddress());
        staff.setAcount(registerRequest.getAcount());
        staff.setPassword(registerRequest.getPassword());
        staff.setStatus(1);

        staffRepository.save(staff);

        // Trả về JSON khi thành công
        return ResponseEntity.ok(Map.of("success", true, "message", "Đăng ký thành công!"));
    }

}
