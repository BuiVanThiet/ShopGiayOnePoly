package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail,Integer> {
    @Query("select bdt from BillDetail bdt where bdt.bill.id = :idBill order by bdt.createDate desc ")
    Page<BillDetail> getBillDetailByIdBill(@Param("idBill") Integer idBills, Pageable pageable);
    @Query("select pdt from ProductDetail pdt")
    List<ProductDetail> getAllProductDetail();
    @Query("select pdt from ProductDetail pdt where pdt.id = :idCheck")
    ProductDetail getProductDetailById(@Param("idCheck") Integer idCheck);

    @Query("select bdt.id from BillDetail bdt where bdt.bill.id = :idBillCheck and bdt.productDetail.id = :idPDTCheck")
    Integer getBillDetailExist(@Param("idBillCheck") Integer idBillCheck, @Param("idPDTCheck") Integer idPDTCheck);
    @Query("select bdt from BillDetail bdt where bdt.bill.id = :idBill")
    List<BillDetail> getBillDetailByIdBill(@Param("idBill") Integer idBills);

    @Query("select bdt.id from BillDetail bdt where bdt.bill.id = :idBill order by bdt.createDate desc")
    Integer getFirstBillDetailIdByIdBill(@Param("idBill") Integer idBill);

    //    @Query("select pdt from ProductDetail pdt " +
////            "join pdt.product p " +
//            "left join pdt.product.categories cate " +
//            "where (:product is null or pdt.product.nameProduct like %:product%)" +
//            "and (:color is null or pdt.color.id = :color) " +
//            "and (:size is null or pdt.size.id = :size) " +
//            "and (:material is null or pdt.product.material.id = :material) " +
//            "and (:manufacturer is null or pdt.product.manufacturer.id = :manufacturer) " +
//            "and (:origin is null or pdt.product.origin.id = :origin) " +
//            "and ((:categories is null or :categories = '') or cate.id in (:categories)) " +  // Điều kiện với categories
//            "and pdt.status <> 0 and pdt.product.status <> 0")
//    List<ProductDetail> findProductDetailSale(@Param("product") String nameProduct,
//                                              @Param("color") Integer idColor,
//                                              @Param("size") Integer idSize,
//                                              @Param("material") Integer idMaterial,
//                                              @Param("manufacturer") Integer idManufacturer,
//                                              @Param("origin") Integer idOrigin
//                                                ,@Param("categories") List<Integer> idCategory
//                                              );
//@Query("""
//    SELECT
//        new com.example.shopgiayonepoly.dto.response.bill.ProductDetailSaleResponse(
//            pd.id,
//            pd.createDate,
//            pd.updateDate,
//            pd.status,
//            pd.product,
//            pd.color,
//            pd.size,
//            CASE
//                WHEN pd.saleProduct IS NOT NULL AND pd.saleProduct.discountType = 1
//                    THEN pd.price - (pd.price * pd.saleProduct.discountValue / 100)
//                WHEN pd.saleProduct IS NOT NULL AND pd.saleProduct.discountType = 2
//                    THEN pd.price - pd.saleProduct.discountValue
//                ELSE pd.price
//            END,
//            pd.price,
//            pd.quantity,
//            pd.describe
//
//        )
//    FROM ProductDetail pd
//    left JOIN pd.product p
//    LEFT JOIN p.categories c
//    WHERE p.nameProduct LIKE %:nameProduct%
//    AND (:size IS NULL OR pd.size.id = :size)
//    AND (:color IS NULL OR pd.color.id = :color)
//    AND (:material IS NULL OR p.material.id = :material)
//    AND (:manufacturer IS NULL OR p.manufacturer.id = :manufacturer)
//    AND (:origin IS NULL OR p.origin.id = :origin)
//    AND (:categories IS NULL OR c.id IN :categories)
//    AND pd.status <> 0\s
//    AND p.status <> 0
//
//""")
    @Query("SELECT pd FROM ProductDetail pd " +
            "JOIN pd.product p " +
            "LEFT JOIN p.categories c " +
            "WHERE p.nameProduct LIKE %:nameProduct% "+
            "AND (:size IS NULL OR pd.size.id = :size) " +
            "AND (:color IS NULL OR pd.color.id = :color) " +
            "AND (:material IS NULL OR p.material.id = :material) " +
            "AND (:manufacturer IS NULL OR p.manufacturer.id = :manufacturer) " +
            "AND (:origin IS NULL OR p.origin.id = :origin) " +
            "AND (:categories IS NULL OR  c.id IN :categories) " +
            "AND pd.status <> 0 AND p.status <> 0 ")
    List<ProductDetail> findProductDetailSale(
            @Param("nameProduct") String nameProduct,
            @Param("size") Integer size,
            @Param("color") Integer color,
            @Param("material") Integer material,
            @Param("manufacturer") Integer manufacturer,
            @Param("origin") Integer origin,
            @Param("categories") List<Integer> categories
    );

    @Query("select sum(bdt.totalAmount) from BillDetail bdt where bdt.bill.id = :idCheck")
    BigDecimal getTotalAmountByIdBill(@Param("idCheck") Integer id);

    @Query("select cate from Category cate where cate.status <> 0")
    List<Category> getAllCategores();
}
