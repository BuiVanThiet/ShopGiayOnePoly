package com.example.shopgiayonepoly.repositores.attribute;

import com.example.shopgiayonepoly.entites.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {

}
