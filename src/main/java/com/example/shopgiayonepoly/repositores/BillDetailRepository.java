package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Bill;
import com.example.shopgiayonepoly.entites.BillDetail;
import com.example.shopgiayonepoly.entites.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Query("select pdt from ProductDetail pdt " +
//            "left join pdt.product p " +
//            "left join pdt.color c " +
//            "left join pdt.size s " +
//            "left join p.material m " +
//            "left join p.manufacturer mf " +
//            "left join p.origin o " +
//            "left join p.categories cat " +
            "where (:product is null or pdt.product.nameProduct like %:product%)" +
            "and (:color is null or pdt.color.id = :color) " +
            "and (:size is null or pdt.size.id = :size) " +
            "and (:material is null or pdt.product.material.id = :material) " +
            "and (:manufacturer is null or pdt.product.manufacturer.id = :manufacturer) " +
            "and (:origin is null or pdt.product.origin.id = :origin) " +
            "and (:categories is null or pdt.product.categories.size in (:categories))" +
            "and pdt.status <> 2 and pdt.product.status <> 2")
    Page<ProductDetail> getProductDetailSale(@Param("product") String nameProduct,
                                             @Param("color") Integer idColor,
                                             @Param("size") Integer idSize,
                                             @Param("material") Integer idMaterial,
                                             @Param("manufacturer") Integer idManufacturer,
                                             @Param("origin") Integer idOrigin,
                                             @Param("categories") List<Integer> idCategory,
                                             Pageable pageable);
    @Query("select pdt from ProductDetail pdt " +
            "left join pdt.product p " +
            "left join pdt.color c " +
            "left join pdt.size s " +
            "left join p.material m " +
            "left join p.manufacturer mf " +
            "left join p.origin o " +
            "left join p.categories cat " +
            "where (:product is null or p.nameProduct like %:product%)" +
            "and (:color is null or c.id = :color) " +
            "and (:size is null or s.id = :size) " +
            "and (:material is null or m.id = :material) " +
            "and (:manufacturer is null or mf.id = :manufacturer) " +
            "and (:origin is null or o.id = :origin) " +
            "and (:categories is null or cat.id in (:categories))")
    Integer getProductDetailSale(@Param("product") String nameProduct,
                                             @Param("color") Integer idColor,
                                             @Param("size") Integer idSize,
                                             @Param("material") Integer idMaterial,
                                             @Param("manufacturer") Integer idManufacturer,
                                             @Param("origin") Integer idOrigin,
                                             @Param("categories") List<Integer> idCategory);
}
