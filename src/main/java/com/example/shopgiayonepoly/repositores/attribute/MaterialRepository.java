package com.example.shopgiayonepoly.repositores.attribute;

import com.example.shopgiayonepoly.entites.Material;
import com.example.shopgiayonepoly.entites.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {

}
