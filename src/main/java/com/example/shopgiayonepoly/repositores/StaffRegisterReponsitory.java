package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRegisterReponsitory extends JpaRepository<Staff, Integer> {
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END FROM Staff s WHERE s.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END FROM Staff s WHERE s.acount = :acount")
    boolean existsByAcount(@Param("acount") String acount);
}
