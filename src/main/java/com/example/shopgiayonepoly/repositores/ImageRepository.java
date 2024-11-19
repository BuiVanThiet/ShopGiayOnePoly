package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {


}
