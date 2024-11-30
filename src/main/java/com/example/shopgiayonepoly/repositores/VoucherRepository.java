package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.response.VoucherResponse;
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
    @Query(value = "UPDATE voucher SET end_date = DATEADD(day, 1, CONVERT(date, GETDATE())), status = 1,update_date = GETDATE() WHERE id = :id", nativeQuery = true)
    public void updateVoucherExpired(@Param("id") Integer id);



    @Modifying
    @Transactional
    @Query(value = "update Voucher set status =0, updateDate = CURRENT_TIMESTAMP where id=:id")
    public void deleteBySetStatus(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("update Voucher v set v.status=1, v.updateDate = CURRENT_TIMESTAMP where  v.id =:id")
    public void restoreStatusVoucher(@Param("id") Integer id);

    @Query("select v from Voucher v where (v.nameVoucher like %:key% or v.codeVoucher like %:key%) and v.status = 1")
    public Page<Voucher> searchVoucherByKeyword(@Param("key") String key, Pageable pageable);


    @Query("select v from Voucher v where v.discountType =:types and v.status = 1")
    Page<Voucher> searchVoucherByTypeVoucher(@Param("types") int type, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Voucher v set v.status = 2, v.updateDate = CURRENT_TIMESTAMP where v.endDate < CURRENT_TIMESTAMP and v.status <> 2")
    void updateVoucherStatusForExpired();

    @Query("""
       select new com.example.shopgiayonepoly.dto.response.VoucherResponse(
              v.id,
              v.codeVoucher,
              v.nameVoucher,
              v.discountType,
              v.priceReduced,
              v.pricesApply,
              v.pricesMax,
              v.startDate,
              v.endDate,
              v.describe,
              v.quantity,
              v.status,
              v.createDate,
              v.updateDate
              )
       from Voucher v
       where v.id = :id
       """)
    public VoucherResponse getDetailVoucherByID(@Param("id") Integer id);

    @Query("SELECT v FROM Voucher v " +
           "WHERE v.pricesApply <= :totalPrice " +
           "AND v.startDate <= :currentDate " +
           "AND v.endDate >= :currentDate " +
           "AND v.status=1")
    List<Voucher> findApplicableVouchers(
            @Param("totalPrice") BigDecimal totalPrice,
            @Param("currentDate") LocalDate currentDate);

    /////////////
    @Query(value = """
    SELECT
    	id,
    	code_voucher,
    	name_voucher,
    	CASE
               WHEN discount_type = 1 THEN N'Theo %'
               WHEN discount_type = 2 THEN N'Theo tiền'
               ELSE N'Không xác định'
        END AS discount_type_desc,
    	CASE
            WHEN discount_type = 1 THEN FORMAT(price_reduced, 'N0', 'vi-VN') + ' %'  -- Nếu discount_type = 1 thì hiển thị phần trăm
            ELSE FORMAT(price_reduced, 'N0', 'vi-VN') + ' VNĐ'  -- Nếu discount_type không phải 1 thì hiển thị VNĐ
        END AS price_reduced_formatted,    FORMAT(prices_apply, 'N0', 'vi-VN') + ' VNĐ' AS prices_apply_formatted,
        FORMAT(prices_max, 'N0', 'vi-VN') + ' VNĐ' AS prices_max_formatted,
    	start_date,
    	end_date,
    	quantity,
    	CASE
           WHEN status = 1 and CONVERT(DATE, GETDATE()) > CONVERT(DATE, end_date) THEN 3
           ELSE status
        END AS status_desc
    FROM voucher
    WHERE
        -- 1. Lọc theo discountType, nếu null thì hiện tất cả
        (:typeCheck IS NULL OR discount_type = :typeCheck)
        -- 2. Lọc gần đúng khi nhập ký tự của trường codeVoucher và nameVoucher
        AND CONCAT(name_voucher, code_voucher) LIKE CONCAT('%', :searchTerm, '%')
        -- 3. Lọc theo 3 trạng thái: hoạt động, ngừng hoạt động, hết hạn
        AND (
            (:status = 1 AND status = 1 and (CONVERT(DATE, GETDATE()) <= CONVERT(DATE, end_date))) -- hoạt động
            OR (:status = 2 AND (status = 0 OR status = 2)) -- ngừng hoạt động
            OR (:status = 3 AND CONVERT(DATE, GETDATE()) > CONVERT(DATE, end_date) and status = 1) -- hết hạn
        ) 
        ORDER BY update_date DESC;
""",nativeQuery = true)
    public List<Object[]> getVoucherFilter(@Param("typeCheck") Integer typeCheck, @Param("searchTerm") String searchTerm, @Param("status") Integer status);
}
