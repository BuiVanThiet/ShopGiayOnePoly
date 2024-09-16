package com.example.shopgiayonepoly.repositores.attribute;

import com.example.shopgiayonepoly.entites.Origin;
import com.example.shopgiayonepoly.entites.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OriginRepository extends JpaRepository<Origin, Integer> {

}
