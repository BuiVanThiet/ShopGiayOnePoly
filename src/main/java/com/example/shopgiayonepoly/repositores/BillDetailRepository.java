package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Bill;
import com.example.shopgiayonepoly.entites.BillDetail;
import com.example.shopgiayonepoly.entites.ProductDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail,Integer> {
    @Query("select bdt from BillDetail bdt where bdt.bill.id = :idBill order by bdt.createDate desc ")
    List<BillDetail> getBillDetailByIdBill(@Param("idBill") Integer idBills, Pageable pageable);
    @Query("select pdt from ProductDetail pdt")
    List<ProductDetail> getAllProductDetail();
    @Query("select pdt from ProductDetail pdt where pdt.id = :idCheck")
    ProductDetail getProductDetailById(@Param("idCheck") Integer idCheck);
}
