package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffSecurityRepository extends JpaRepository<Staff, Integer> {
    @Query("SELECT s FROM Staff s WHERE s.acount = :acount OR s.email = :email")
    Staff findByAcountOrEmail(@Param("acount") String acount, @Param("email") String email);
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(codeStaff, 4, LEN(codeStaff) - 2) AS INTEGER)), 0) FROM Staff")
    Integer findLastCode();
}
