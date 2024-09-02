package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail,Integer> {
}
