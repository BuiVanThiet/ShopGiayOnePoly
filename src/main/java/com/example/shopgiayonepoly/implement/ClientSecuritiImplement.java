package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.repositores.ClientSecurityResponsetory;
import com.example.shopgiayonepoly.repositores.StaffSecurityRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class ClientSecuritiImplement implements UserDetailsService {

    @Autowired
    private ClientSecurityResponsetory clientSecurityResponsetory;

    @Override
    public UserDetails loadUserByUsername(String acountOrEmail) throws UsernameNotFoundException {
        // Tìm người dùng theo tài khoản hoặc email
        ClientLoginResponse clientLoginResponse = clientSecurityResponsetory.getCustomerByEmailAndAcount(acountOrEmail, acountOrEmail);
        if (clientLoginResponse == null) {
            throw new UsernameNotFoundException("User not found: " + acountOrEmail);
        }

        // Tạo đối tượng UserDetails để Spring Security sử dụng cho xác thực
        return User.withUsername(clientLoginResponse.getAcount())
                .password(passwordEncoder().encode(clientLoginResponse.getPassword()))  // Mật khẩu đã mã hóa từ cơ sở dữ liệu
                .roles(clientLoginResponse.getRole())  // Vai trò người dùng
                .build();
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

