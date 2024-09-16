package com.example.shopgiayonepoly.repositores.attribute;

import com.example.shopgiayonepoly.entites.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {

}
