package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface roleReponsitory extends JpaRepository<Role, Integer> {
    @Query("select r from Role r where r.nameRole = :namerole")
    Role findByNameRole(String namerole);
}
