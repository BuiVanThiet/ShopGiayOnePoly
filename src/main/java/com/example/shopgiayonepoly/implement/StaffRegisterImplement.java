package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.repositores.StaffRegisterReponsitory;
import com.example.shopgiayonepoly.service.StaffRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffRegisterImplement implements StaffRegisterService {
    @Autowired
    StaffRegisterReponsitory staffRegisterReponsitory;

    @Override
    public boolean existsByEmail(String email) {
        return staffRegisterReponsitory.existsByEmail(email);
    }

    @Override
    public boolean existsByAcount(String acount) {
        return staffRegisterReponsitory.existsByAcount(acount);
    }
}
