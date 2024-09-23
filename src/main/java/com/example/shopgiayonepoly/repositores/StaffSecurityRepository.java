package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffSecurityRepository extends JpaRepository<Staff, Integer> {
    Staff findByAcountOrEmail(String acount, String email);
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(codeStaff, 4, LEN(codeStaff) - 2) AS INTEGER)), 0) FROM Staff")
    Integer findLastCode();
}
