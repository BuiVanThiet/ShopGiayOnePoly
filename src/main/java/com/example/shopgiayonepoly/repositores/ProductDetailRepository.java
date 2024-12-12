package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    @Query("select product_detail from ProductDetail product_detail where product_detail.status <> 0")
    List<ProductDetail> getProductDetailNotStatus0();

    @Query("select product_detail from ProductDetail product_detail where product_detail.status = 0")
    List<ProductDetail> getProductDetailDelete();

}
