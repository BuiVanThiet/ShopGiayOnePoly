package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleReponsitory extends JpaRepository<Role, Integer> {
    @Query("select r from Role r where r.nameRole = :namerole")
    Role findByNameRole(String namerole);

    @Query("select r from Role r where r.status = 1")
    List<Role> getListRole();
}
