package com.example.shopgiayonepoly.repositores;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleProductRepository extends JpaRepository<com.example.shopgiayonepoly.entites.SaleProduct,Integer> {
}
