package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("select product from Product product where product.status <> 0")
    List<Product> getProductNotStatus0();

    @Query("select product from Product product where product.status = 0")
    List<Product> getProductDelete();

    @Query("select product from Product product where product.codeProduct = :codeProduct")
    Optional<Product> getOneProductByCodeProduct(@Param("codeProduct") String codeProduct);

    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN FETCH p.images " +
            "LEFT JOIN FETCH p.categories " +
            "WHERE p.id = :id")
    Optional<Product> getOneByID(@Param("id") Integer id);




}
