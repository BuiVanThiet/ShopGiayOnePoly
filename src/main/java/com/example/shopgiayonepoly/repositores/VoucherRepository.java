package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Voucher;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    @Query("select v from Voucher v where v.status =1")
    public Page<Voucher> getAllVoucherByPage(Pageable pageable);

    @Query("select v from Voucher v where v.status =1")
    public List<Voucher> getAllVoucher();

    @Query("select v from Voucher v where v.status =0")
    public Page<Voucher> getVoucherDeleteByPage(Pageable pageable);

    @Query("select v from Voucher v where v.status =0")
    public List<Voucher> getAllVoucherDelete();

    @Query("select v from Voucher v where v.status =2")
    public Page<Voucher> getVoucherExpiredByPage(Pageable pageable);
    @Modifying
    @Transactional
    @Query(value = "UPDATE voucher SET end_date = DATEADD(day, 1, CONVERT(date, GETDATE())), status = 1 WHERE id = :id", nativeQuery = true)
    public void updateVoucherExpired(@Param("id") Integer id);



    @Modifying
    @Transactional
    @Query(value = "update Voucher set status =0 where id=:id")
    public void deleteBySetStatus(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("update Voucher set status=1 where  id =:id")
    public void restoreStatusVoucher(@Param("id") Integer id);

    @Query("select v from Voucher v where (v.nameVoucher like %:key% or v.codeVoucher like %:key%) and v.status = 1")
    public Page<Voucher> searchVoucherByKeyword(@Param("key") String key, Pageable pageable);


    @Query("select v from Voucher v where v.discountType =:types and v.status = 1")
    Page<Voucher> searchVoucherByTypeVoucher(@Param("types") int type, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Voucher v set v.status = 2 where v.endDate < CURRENT_TIMESTAMP and v.status <> 2")
    public void updateVoucherStatusForExpired();

//    @Query("select from ")



}
