package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.entites.SaleProduct;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleProductRepository extends JpaRepository<SaleProduct, Integer> {
    @Query("select s from SaleProduct s where s.status =1")
    public Page<SaleProduct> getAllSaleProductByPage(Pageable pageable);

    @Query("select s from SaleProduct s where s.status =1")
    public List<SaleProduct> getAllSaleProduct();

    @Query("select s from SaleProduct s where s.status =0")
    public Page<SaleProduct> getSaleProductDeleteByPage(Pageable pageable);

    @Query("select s from SaleProduct s where s.status =0")
    public List<SaleProduct> getAllSaleProductDelete();

    @Modifying
    @Transactional
    @Query(value = "update SaleProduct set status =0 where id=:id")
    public void deleteBySetStatus(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("update SaleProduct set status=1 where  id =:id")
    public void restoreStatusSaleProduct(@Param("id") Integer id);

    @Query("select s from SaleProduct s where (s.nameSale like %:key% or s.codeSale like %:key%) and s.status = 1")
    public Page<SaleProduct> searchSaleProductByKeyword(@Param("key") String key, Pageable pageable);

    @Query("select s from SaleProduct s where s.discountType =:types and s.status = 1")
    Page<SaleProduct> searchSaleProductByTypeSaleProduct(@Param("types") int type, Pageable pageable);

    @Query("select p from ProductDetail p")
    public List<ProductDetail> getAllProductDetailByPage();

}
