package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffSecurityRepository extends JpaRepository<Staff, Integer> {
    public Staff findByAcountOrEmail(String acount, String email);
}
