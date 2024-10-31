package com.example.shopgiayonepoly.service;

import org.springframework.data.repository.query.Param;

public interface StaffRegisterService {
    boolean existsByEmail(@Param("email") String email);
    boolean existsByAcount(@Param("acount") String acount);

}
