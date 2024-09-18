package com.example.shopgiayonepoly.repositores.attribute;

import com.example.shopgiayonepoly.entites.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
    @Query("select client from Material client where client.status <> 0")
    List<Material> getClientNotStatus0();
}
