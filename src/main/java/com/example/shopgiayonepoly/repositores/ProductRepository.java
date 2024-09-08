package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
