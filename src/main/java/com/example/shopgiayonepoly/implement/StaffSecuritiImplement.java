//package com.example.shopgiayonepoly.implement;
//
//import com.example.shopgiayonepoly.entites.Staff;
//import com.example.shopgiayonepoly.repositores.StaffRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class StaffSecuritiImplement implements UserDetailsService {
//    @Autowired
//    StaffRepository staffRepository;
//    @Override
//    public UserDetails loadUserByUsername(String acountOrEmail) throws UsernameNotFoundException {
//        Staff staff = staffRepository.findByAcountOrEmail(acountOrEmail, acountOrEmail);
//        if(staff != null){
//            String username = (staff.getAcount() != null) ? staff.getAcount() : staff.getEmail();
//
//            var springStaff = User.withUsername(username)
//                    .password(staff.getPassword())
//                    .roles(staff.getRole().getNameRole())
//                    .build();
//            return springStaff;
//        }
//        return null;
//    }
//}
