package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.Staff;
import org.springframework.data.repository.query.Param;

public interface CustomerRegisterService {
    boolean existsByEmail(@Param("email") String email);

    boolean existsByAcount(@Param("acount") String acount);
    Customer findByAcount(@Param("acount") String acount);
    <S extends Customer> S save(S entity);
}
