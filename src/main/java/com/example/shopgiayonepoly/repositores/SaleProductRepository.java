package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.SaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleProductRepository extends JpaRepository<SaleProduct,Integer> {
}
