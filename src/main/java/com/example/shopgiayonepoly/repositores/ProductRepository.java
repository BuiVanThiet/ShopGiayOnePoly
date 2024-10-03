package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("select product from Product product where product.status <> 0")
    List<Product> getProductNotStatus0();

    @Query("select product from Product product where product.status = 0")
    List<Product> getProductDelete();
}
