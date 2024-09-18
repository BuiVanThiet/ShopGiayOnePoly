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
public interface VoucherRepository extends JpaRepository<Voucher,Integer> {
    @Query("select v from Voucher v where v.status =1")
    public Page<Voucher> getAllVoucherByPage(Pageable pageable);
    @Query("select v from Voucher v where v.status =1")
    public List<Voucher> getAllVoucher();
    @Modifying
    @Transactional
    @Query(value = "update voucher set status =0 where id=:id",nativeQuery = true)
    public void deleteBySetStatus(@Param("id")Integer id);
}
