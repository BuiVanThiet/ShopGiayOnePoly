package com.example.shopgiayonepoly.repositores;

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
public interface SaleProductRepository extends JpaRepository<com.example.shopgiayonepoly.entites.SaleProduct, Integer> {
    @Query("select s from SaleProduct s where s.status =1")
    public Page<SaleProduct> getAllSaleProductByPage(Pageable pageable);

    @Query("select s from SaleProduct s where s.status =1")
    public List<SaleProduct> getAllSaleProduct();

    @Query("select s from SaleProduct s where s.status =0")
    public Page<SaleProduct> getSaleProductDeleteByPage(Pageable pageable);

    @Query("select s from SaleProduct s where s.status =0")
    public List<SaleProduct> getAllSaleProductDelete();

    @Query("select s from SaleProduct s where s.status =2")
    public Page<SaleProduct> getSaleProductExpiredByPage(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE sale_product SET end_date = DATEADD(day, 1, CONVERT(date, GETDATE())), status = 1 WHERE id = :id", nativeQuery = true)
    public void updateSaleProductExpired(@Param("id") Integer id);


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

    @Modifying
    @Transactional
    @Query("update SaleProduct s set s.status = 2 where s.endDate < CURRENT_DATE and s.status <> 2")
    public void updateSaleProductStatusForExpired();
}
