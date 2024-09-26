package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.repositores.StaffSecurityRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Service
public class StaffSecuritiImplement implements UserDetailsService {
    @Autowired
    StaffSecurityRepository staffRepository;
    @Override
    public UserDetails loadUserByUsername(String acountOrEmail) throws UsernameNotFoundException {
        Staff staff = staffRepository.findByAcountOrEmail(acountOrEmail, acountOrEmail);
        if(staff != null){

            // Lấy HttpServletRequest từ RequestContextHolder
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            HttpSession session = request.getSession();  // Lấy session

            // Lưu đối tượng staff vào session
            session.setAttribute("staffLogin", staff);

            String username = (staff.getAcount() != null) ? staff.getAcount() : staff.getEmail();
            String role = staff.getRole() != null ? staff.getRole().getNameRole() : "USER";

            var springStaff = User.withUsername(username)
                    .password(this.passwordEncoder().encode(staff.getPassword()))
                    .roles(role)
                    .build();

            return springStaff;
        }
        return null;
    }
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
