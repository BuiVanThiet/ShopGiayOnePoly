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

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    @Query("select v from Voucher v where v.status =1")
    public Page<Voucher> getAllVoucherByPage(Pageable pageable);

    @Query("select v from Voucher v where v.status =1")
    public List<Voucher> getAllVoucher();

    @Query("select v from Voucher v where v.status =2")
    public Page<Voucher> getVoucherDeleteByPage(Pageable pageable);

    @Query("select v from Voucher v where v.status =2")
    public List<Voucher> getAllVoucherDelete();

    @Modifying
    @Transactional
    @Query(value = "update Voucher set status =2 where id=:id")
    public void deleteBySetStatus(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("update Voucher set status=1 where  id =:id")
    public void restoreStatusVoucher(@Param("id") Integer id);

    @Query("select v from Voucher v where v.nameVoucher like %:key% or v.codeVoucher like %:key%")
    public Page<Voucher> searchVoucherByKeyword(@Param("key") String key, Pageable pageable);


}
