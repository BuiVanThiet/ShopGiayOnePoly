package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.repositores.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class StaffService implements UserDetailsService {
    @Autowired
    StaffRepository staffRepository;
    @Override
    public UserDetails loadUserByUsername(String acount) throws UsernameNotFoundException {
        Staff staff = staffRepository.findByAcount(acount);
        if(staff != null){
            var springStaff = User.withUsername(staff.getAcount())
                    .password(staff.getPassword())
                    .roles(staff.getRole().getNameRole())
                    .build();
            return springStaff;
        }
        return null;
    }
}