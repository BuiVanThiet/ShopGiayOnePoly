package com.example.shopgiayonepoly.repositores.attribute;

import com.example.shopgiayonepoly.entites.Material;
import com.example.shopgiayonepoly.entites.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
    @Query("select material from Material material where material.status <> 0 order by material.id desc ")
    List<Material> getMaterialNotStatus0();

    @Query("select material from Material material where material.status = 0")
    List<Material> getMaterialDelete();
}
