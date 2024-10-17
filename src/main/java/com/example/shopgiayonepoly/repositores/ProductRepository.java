package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.CategoryProduct;
import com.example.shopgiayonepoly.entites.Image;
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

    @Query("select product from Product product where product.id = :id")
    Optional<Product> getOneProductByID(@Param("id") Integer id);

    @Query("SELECT image FROM Image image WHERE image.product.id = :productId")
    List<Image> findAllImagesByProductId(@Param("productId") Integer productId);

    @Query("SELECT categoryProduct FROM CategoryProduct categoryProduct WHERE categoryProduct.product.id = :productId")
    List<CategoryProduct> findAllCategoryByProductId(@Param("productId") Integer productId);




}
