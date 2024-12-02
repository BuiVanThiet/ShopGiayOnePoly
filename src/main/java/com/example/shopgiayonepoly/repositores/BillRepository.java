package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.response.bill.*;
import com.example.shopgiayonepoly.entites.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill,Integer> {
    //ngay 3thang9
    @Query("select b from Bill b where b.status = 0 order by b.createDate desc")
    List<Bill> getBillByStatusNew(Pageable pageable);
    @Query("select client from Customer client where client.status = 1")
    List<Customer> getClientNotStatus0();

    @Query("""
    select new com.example.shopgiayonepoly.dto.response.bill.BillTotalInfornationResponse(
        b.id,
        b.totalAmount,
        v.id,
        v.codeVoucher,
        v.nameVoucher,
        case 
            when v.discountType = 1 then
                case 
                    when b.totalAmount * (v.priceReduced / 100) > v.pricesMax then
                        v.pricesMax
                    else 
                        b.totalAmount * (v.priceReduced / 100)
                end 
            when v.discountType = 2 then
                v.priceReduced
            else 
                0.00
        end,
        case 
            when v.discountType = 1 then
                case 
                    when b.totalAmount * (v.priceReduced / 100) > v.pricesMax then
                        b.totalAmount - v.pricesMax
                    else 
                        b.totalAmount - (b.totalAmount * (v.priceReduced / 100))
                end 
            when v.discountType = 2 then
                b.totalAmount - v.priceReduced
            else 
                b.totalAmount
        end,
        b.note
    )
    from Bill b 
    left join b.voucher v 
    where b.id = :billId
""")
    BillTotalInfornationResponse findBillVoucherById(@Param("billId") Integer billId);

    @Query("SELECT v FROM Voucher v " +
            "WHERE (SELECT b.totalAmount FROM Bill b WHERE b.id = :idBillCheck) >= v.pricesApply " +
            "AND v.status <> 0 " +
            "AND (v.id <> (SELECT b.voucher.id FROM Bill b WHERE b.id = :idBillCheck) OR (SELECT b.voucher.id FROM Bill b WHERE b.id = :idBillCheck) IS NULL) " +
            "and (CURRENT_TIMESTAMP between v.startDate and v.endDate) " +
            "AND CONCAT(v.nameVoucher, v.codeVoucher) LIKE %:keyword% " +
            "")
    Page<Voucher> getVoucherByBill(@Param("idBillCheck") Integer idBillCheck, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT v FROM Voucher v " +
            "WHERE (SELECT b.totalAmount FROM Bill b WHERE b.id = :idBillCheck) >= v.pricesApply " +
            "AND v.status <> 0 " +
            "AND (v.id <> (SELECT b.voucher.id FROM Bill b WHERE b.id = :idBillCheck) OR (SELECT b.voucher.id FROM Bill b WHERE b.id = :idBillCheck) IS NULL) " +
            "and (CURRENT_TIMESTAMP between v.startDate and v.endDate) " +
            "AND CONCAT(v.nameVoucher, v.codeVoucher) LIKE %:keyword% " +
            "")
    List<Voucher> getVoucherByBill(@Param("idBillCheck") Integer idBillCheck, @Param("keyword") String keyword);
    @Query(value = """
        SELECT 
            v.id,
            v.code_voucher,
        	v.name_voucher,
        	v.price_reduced,
        	v.discount_type,
        	v.prices_apply,
        	v.quantity,
        	v.status,
            CASE
                WHEN discount_type = 1 THEN
                    CASE
                        WHEN (b.total_amount * (price_reduced / 100)) > prices_max THEN prices_max
                        ELSE b.total_amount * (price_reduced / 100)
                    END
                WHEN discount_type = 2 THEN price_reduced
                ELSE 0
            END AS discount_amount 
        FROM voucher v 
        left JOIN bill b ON b.id = :idBillCheck 
        WHERE v.status <> 0
        AND GETDATE() BETWEEN v.start_date AND v.end_date
        and (v.id <> b.id_voucher or b.id_voucher is null )
        AND v.prices_apply <= b.total_amount
        and concat(v.name_voucher,v.code_voucher) like %:keyword% 
        ORDER BY discount_amount DESC;
""",nativeQuery = true)
    List<Object[]> getVoucherByBillV2(@Param("idBillCheck") Integer idBillCheck, @Param("keyword") String keyword);

    @Query("select new com.example.shopgiayonepoly.dto.response.bill.ClientBillInformationResponse(cuss.fullName,cuss.numberPhone,cuss.email,cuss.addRess,cuss.addRess,cuss.addRess,cuss.addRess) from Customer cuss where cuss.id = :idClient")
    List<ClientBillInformationResponse> getClientBillInformationResponse(@Param("idClient") Integer idBill);
    // phan nay danh cho quan ly hoa don

    //    @Query("""
//        select
//        new com.example.shopgiayonepoly.dto.response.bill.BillResponseManage(
//            bill.id,
//            bill.codeBill,
//            bill.customer,
//            bill.staff,
//            bill.addRess,
//            bill.voucher,
//            bill.shippingPrice,
//            bill.cash,
//            bill.acountMoney,
//            bill.note,
//            case
//            when voucher.discountType = 1 then
//                case
//                    when bill.totalAmount * (voucher.priceReduced / 100) > voucher.pricesMax then
//                        bill.totalAmount - voucher.pricesMax + bill.shippingPrice
//                    else
//                        bill.totalAmount - (bill.totalAmount * (voucher.priceReduced / 100)) + bill.shippingPrice
//                end
//            when voucher.discountType = 2 then
//                bill.totalAmount - voucher.priceReduced + bill.shippingPrice
//            else
//                bill.totalAmount + bill.shippingPrice
//        end,
//            bill.paymentMethod,
//            bill.billType,
//            bill.paymentStatus,
//            bill.surplusMoney,
//            bill.createDate,
//            bill.updateDate,
//            bill.status)
//        from Bill bill
//        LEFT JOIN bill.customer customer
//        LEFT JOIN bill.staff staff
//        LEFT JOIN bill.voucher voucher
//        where
//        bill.status <> 0 and
//        (concat(COALESCE(bill.codeBill, ''), COALESCE(bill.customer.fullName, ''), COALESCE(bill.customer.numberPhone, '')) like %:nameCheck%)
//         and (:statusCheck is null or (bill.status = :statusCheck))
//    """)
//    Page<BillResponseManage> getAllBillByStatusDiss0(@Param("nameCheck") String nameCheck, @Param("statusCheck") Integer statusCheck, Pageable pageable);
    @Query("""
        select 
        new com.example.shopgiayonepoly.dto.response.bill.BillResponseManage(
            bill.id, 
            case
                when bill.status between 0 and 6 then bill.codeBill
                else rb.codeReturnBillExchangBill
            end
            , 
            bill.customer, 
            bill.staff, 
            bill.addRess, 
            bill.voucher, 
            bill.shippingPrice, 
            bill.cash, 
            bill.acountMoney, 
            bill.note, 
            case
                when bill.status between 0 and 6 then bill.totalAmount - bill.priceDiscount + bill.shippingPrice
                else 0-((rb.customerRefund - rb.exchangeAndReturnFee) - (rb.customerPayment - rb.discountedAmount))
            end
            , 
            bill.paymentMethod, 
            bill.billType, 
            bill.paymentStatus, 
            bill.surplusMoney, 
            bill.createDate, 
            bill.updateDate,
            bill.status) 
        from Bill bill 
        left join ReturnBillExchangeBill rb on rb.bill.id = bill.id
        LEFT JOIN bill.customer customer
        LEFT JOIN bill.staff staff
        LEFT JOIN bill.voucher voucher
        where
            bill.status <> 0 and
            (concat(COALESCE(bill.codeBill, ''), COALESCE(bill.customer.fullName, ''), COALESCE(bill.customer.numberPhone, '')) like %:nameCheck%)
            AND (:statusCheck IS NULL OR bill.status IN (:statusCheck))
            AND (bill.updateDate between :startDate and :endDate)
            order by bill.updateDate desc
    """)
    Page<BillResponseManage> getAllBillByStatusDiss0(
            @Param("nameCheck") String nameCheck,
            @Param("statusCheck") List<Integer> statusCheck,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            Pageable pageable);
    //    @Query("""
//        select
//        new com.example.shopgiayonepoly.dto.response.bill.BillResponseManage(
//            bill.id, bill.codeBill, bill.customer, bill.staff, bill.addRess, bill.voucher,
//            bill.shippingPrice, bill.cash, bill.acountMoney, bill.note, bill.totalAmount,
//            bill.paymentMethod, bill.billType, bill.paymentStatus, bill.surplusMoney,
//            bill.createDate, bill.updateDate, bill.status)
//        from Bill bill
//        LEFT JOIN bill.customer customer
//        LEFT JOIN bill.staff staff
//        LEFT JOIN bill.voucher voucher
//        where
//         bill.status <> 0 and
//         (concat(COALESCE(bill.codeBill, ''), COALESCE(bill.customer.fullName, ''), COALESCE(bill.customer.numberPhone, '')) like %:nameCheck%)
//         and (:statusCheck is null or (bill.status = :statusCheck))
//    """)
//    List<BillResponseManage> getAllBillByStatusDiss0(@Param("nameCheck") String nameCheck, @Param("statusCheck") Integer statusCheck);
    @Query("""
            select 
            new com.example.shopgiayonepoly.dto.response.bill.BillResponseManage(
                bill.id, bill.codeBill, bill.customer, bill.staff, bill.addRess, bill.voucher, 
                bill.shippingPrice, bill.cash, bill.acountMoney, bill.note, bill.totalAmount, 
                bill.paymentMethod, bill.billType, bill.paymentStatus, bill.surplusMoney, 
                bill.createDate, bill.updateDate, bill.status) 
            from Bill bill 
            LEFT JOIN bill.customer customer
            LEFT JOIN bill.staff staff
            LEFT JOIN bill.voucher voucher
            where
                bill.status <> 0 and
                (concat(COALESCE(bill.codeBill, ''), COALESCE(bill.customer.fullName, ''), COALESCE(bill.customer.numberPhone, '')) like %:nameCheck%)
                AND (:statusCheck IS NULL OR bill.status IN (:statusCheck))
                AND (bill.updateDate between :startDate and :endDate)
                order by bill.updateDate desc
        """)
    List<BillResponseManage> getAllBillByStatusDiss0(
            @Param("nameCheck") String nameCheck,
            @Param("statusCheck") List<Integer> statusCheck,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
            );
//    @Query("""
//    select
//     new com.example.shopgiayonepoly.dto.response.bill.InformationBillByIdBillResponse(
//     b.id,
//     b.createDate,
//     b.updateDate,
//     b.status,
//     b.codeBill,
//     b.billType,
//     b.shippingPrice,
//     b.totalAmount,
//     v,
//     case
//            when v.discountType = 1 then
//                case
//                    when b.totalAmount * (v.priceReduced / 100) > v.pricesMax then
//                        v.pricesMax
//                    else
//                        b.totalAmount * (v.priceReduced / 100)
//                end
//            when v.discountType = 2 then
//                v.priceReduced
//            else
//                0.00
//        end,
//        b.paymentStatus,
//        b.note
//     )
//     from Bill b
//     left join b.voucher v
//     where b.id = :idCheck
//""")
@Query(""" 
    select 
     new com.example.shopgiayonepoly.dto.response.bill.InformationBillByIdBillResponse(
     b.id,
     b.createDate,
     b.updateDate,
     b.status,
     b.codeBill,
     b.billType,
     b.shippingPrice,
     b.totalAmount,
     v,
     b.priceDiscount,
        b.paymentStatus,
        b.note
     )
     from Bill b
     left join b.voucher v
     where b.id = :idCheck
""")
    InformationBillByIdBillResponse getInformationBillByIdBill(@Param("idCheck") Integer idBill);
    @Query("""
select case 
            when v.discountType = 1 then
                case 
                    when b.totalAmount * (v.priceReduced / 100) > v.pricesMax then
                        v.pricesMax
                    else 
                        b.totalAmount * (v.priceReduced / 100)
                end 
            when v.discountType = 2 then
                v.priceReduced
            else 
                0.00
        end from Bill b left join b.voucher v where b.id = :idBillCheck
""")
    String getDiscountBill(@Param("idBillCheck") Integer idBill);

    @Query(value = """
        (select
            b.id,
            b.update_date,
            (b.cash + b.surplus_money) AS so_tien,
            N'Tiền mặt' AS payment_method,
            CASE
                WHEN LEFT(invo.note, CHARINDEX(',', invo.note) - 1) = N'Không có' THEN N'Không có'
                ELSE ISNULL(
                    (select s.code_staff + '-' + s.full_name
                     from staff s
                     where s.id = (SUBSTRING(invo.note, 1, CHARINDEX(',', invo.note) - 1))),
                    N'Không có'
                )
            END AS staff_info
        from bill b
        join invoice_status invo
        on invo.id_bill = b.id
        where b.id = :idCheck and invo.status = 101 AND b.cash > 0)
        UNION ALL
        (select
            b.id,
            b.update_date,
            b.acount_money AS so_tien,
            ISNULL(N'Tiền tài khoản' + N' - Mã giao dịch: ' + bank_tran_no, N'Tiền tài khoản - Không có') AS payment_method,
            CASE
                WHEN LEFT(invo.note, CHARINDEX(',', invo.note) - 1) = N'Không có' THEN N'Không có'
                ELSE ISNULL(
                    (select s.code_staff + '-' + s.full_name
                     from staff s
                     where s.id = (SUBSTRING(invo.note, 1, CHARINDEX(',', invo.note) - 1))),
                    N'Không có'
                )
            END AS staff_info
        from bill b
        join invoice_status invo
        on invo.id_bill = b.id
        where b.id = :idCheck and invo.status = 101 AND b.acount_money > 0)
        UNION ALL
        (select
            b.id,
            b.update_date,
            b.surplus_money AS so_tien,
            N'Tiền thừa' AS payment_method,
            CASE
                WHEN LEFT(invo.note, CHARINDEX(',', invo.note) - 1) = N'Không có' THEN N'Không có'
                ELSE ISNULL(
                    (select s.code_staff + '-' + s.full_name
                     from staff s
                     where s.id = (SUBSTRING(invo.note, 1, CHARINDEX(',', invo.note) - 1))),
                    N'Không có'
                )
            END AS staff_info
        from bill b
        join invoice_status invo
        on invo.id_bill = b.id
        where b.id = :idCheck and invo.status = 101 AND b.surplus_money > 0)
        UNION ALL
        (select
            re.id AS id,
            re.update_date,
            pe.cash AS so_tien,
            N'Tiền mặt (Đổi-Trả sản phẩm)' AS payment_method,
            ISNULL(
                (select s.code_staff + '-' + s.full_name
                 from staff s
                 where s.id = pe.id_staff),
                N'Không có'
            ) AS staff_info
        from return_bill_exchange_bill re
        join payment_exchange pe
        on re.id = pe.id_exchange
        where re.id_bill = :idCheck AND pe.cash > 0)
        UNION ALL
        (select
            re.id AS id,
            re.update_date,
            pe.cash_acount AS so_tien,
            ISNULL(N'Tiền tài khoản (Đổi-Trả sản phẩm)' + N' - Mã giao dịch: ' + pe.bank_tran_no, N'Tiền tài khoản (Đổi-Trả sản phẩm) - Không có') AS payment_method,
            ISNULL(
                (select s.code_staff + '-' + s.full_name
                 from staff s
                 where s.id = pe.id_staff),
                N'Không có'
            ) AS staff_info
        from return_bill_exchange_bill re
        join payment_exchange pe
        on re.id = pe.id_exchange
        where re.id_bill = 330 AND pe.cash_acount > 0)
        UNION ALL
        (select
            re.id AS id,
            re.update_date,
            pe.surplus_money AS so_tien,
            N'Tiền thừa (Đổi-Trả sản phẩm)' AS payment_method,
            ISNULL(
                (select s.code_staff + '-' + s.full_name
                 from staff s
                 where s.id = pe.id_staff),
                N'Không có'
            ) AS staff_info
        from return_bill_exchange_bill re
        join payment_exchange pe
        on re.id = pe.id_exchange
        where re.id_bill = :idCheck AND pe.surplus_money > 0);
        
""", nativeQuery = true)
    List<Object[]> getInfoPaymentByIdBill(@Param("idCheck") Integer idCheck);

//    @Query(value = """
//    SELECT
//    b.code_bill,
//    b.create_date,
//    -- đã sửa: Kiểm tra nếu tìm thấy dấu phẩy thì mới sử dụng SUBSTRING, nếu không trả về 'Không có'
//    CASE
//        WHEN CHARINDEX(',', b.address) > 0 THEN SUBSTRING(b.address, 1, CHARINDEX(',', b.address) - 1)
//        ELSE 'Không có'
//    END AS full_name,
//
//    -- đã sửa: Tương tự kiểm tra vị trí dấu phẩy tiếp theo, nếu không có trả về 'Không có'
//    CASE
//        WHEN CHARINDEX(',', b.address) > 0 AND CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) > 0 THEN
//            SUBSTRING(b.address, CHARINDEX(',', b.address) + 1, CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) - CHARINDEX(',', b.address) - 1)
//        ELSE 'Không có'
//    END AS number_phone,
//
//    COALESCE(c.email, 'Không có') AS email,
//
//    -- đã sửa: Kiểm tra và sử dụng SUBSTRING khi có đủ dấu phẩy, nếu không trả về 'Không có'
//    CASE
//        WHEN CHARINDEX(',', c.addRess, CHARINDEX(',', c.addRess, CHARINDEX(',', c.addRess) + 1) + 1) > 0 THEN
//            SUBSTRING(c.addRess, CHARINDEX(',', c.addRess, CHARINDEX(',', c.addRess, CHARINDEX(',', c.addRess) + 1) + 1) + 2, LEN(c.addRess))
//        ELSE 'Không có'
//    END AS addRess,
//
//    FORMAT(b.total_amount, 'N2') + ' VNĐ' AS total_amount,
//    FORMAT(b.shipping_price, 'N2') + ' VNĐ' AS shipping_price,
//
//    FORMAT(
//        CASE
//            WHEN v.discount_type = 1 THEN
//                CASE
//                    WHEN b.total_amount * (v.price_reduced / 100) > v.prices_max THEN v.prices_max
//                    ELSE b.total_amount * (v.price_reduced / 100)
//                END
//            WHEN v.discount_type = 2 THEN v.price_reduced
//            ELSE 0
//        END, 'N2') + ' VNĐ' AS discount_value,
//
//    FORMAT(
//        CASE
//            WHEN v.discount_type = 1 THEN
//                CASE
//                    WHEN b.total_amount * (v.price_reduced / 100) > v.prices_max THEN b.total_amount - v.prices_max + b.shipping_price
//                    ELSE b.total_amount - (b.total_amount * (v.price_reduced / 100)) + b.shipping_price
//                END
//            WHEN v.discount_type = 2 THEN b.total_amount - v.price_reduced + b.shipping_price
//            ELSE b.total_amount + b.shipping_price
//        END, 'N2') + ' VNĐ' AS total_after_discount
//    FROM bill b
//    LEFT JOIN customer c ON c.id = b.id_customer
//    LEFT JOIN voucher v ON v.id = b.id_voucher
//    WHERE b.id = :idBill
//""", nativeQuery = true)
@Query(value = """
   SELECT
       b.code_bill,
       b.create_date,
       -- đã sửa: Kiểm tra nếu tìm thấy dấu phẩy thì mới sử dụng SUBSTRING, nếu không trả về 'Không có'
       CASE
       WHEN b.address != N'Không có' THEN
           TRIM(SUBSTRING(b.address, 1, CHARINDEX(',', b.address) - 1))
       ELSE
           CASE
               WHEN b.id_customer IS NOT NULL THEN CAST(c.full_name AS NVARCHAR)
               ELSE N'khách lẻ'
           END
   	END AS name_customer,
       -- đã sửa: Tương tự kiểm tra vị trí dấu phẩy tiếp theo, nếu không có trả về 'Không có'
      CASE
       WHEN b.address != N'Không có' THEN
               TRIM(SUBSTRING(b.address, CHARINDEX(',', b.address) + 1, CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) - CHARINDEX(',', b.address) - 1))
       ELSE
           CASE
               WHEN b.id_customer IS NOT NULL THEN CAST(c.number_phone AS NVARCHAR)
               ELSE N'Không có'
           END
   	END AS number_phone_customer,
       CASE
       WHEN b.address != N'Không có' THEN
           TRIM(SUBSTRING(b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) + 1, CHARINDEX(',', b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) + 1) - CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) - 1))
       ELSE
           CASE
               WHEN b.id_customer IS NOT NULL THEN CAST(c.email AS NVARCHAR)
               ELSE N'Không có'
           END
   	END AS email_customer,
   	CASE
   		WHEN b.address != N'Không có' THEN
   		   TRIM(SUBSTRING(b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) + 1) + 1) + 1) + 1) + 1, LEN(b.address)))
   		ELSE
   			CASE
   				WHEN b.id_customer IS NOT NULL THEN CAST(TRIM(SUBSTRING(c.address, CHARINDEX(',', c.address, CHARINDEX(',', c.address, CHARINDEX(',', c.address) + 1) + 1) + 1, LEN(c.address))) AS NVARCHAR)
   				ELSE N'Không có'
   			END
   	END AS add_ress,
       FORMAT(b.total_amount, 'N2') + ' VNĐ' AS total_amount,
       FORMAT(b.shipping_price, 'N2') + ' VNĐ' AS shipping_price,
       FORMAT(
           CASE
               WHEN v.discount_type = 1 THEN
                   CASE
                       WHEN b.total_amount * (v.price_reduced / 100) > v.prices_max THEN v.prices_max
                       ELSE b.total_amount * (v.price_reduced / 100)
                   END
               WHEN v.discount_type = 2 THEN v.price_reduced
               ELSE 0
           END, 'N2') + ' VNĐ' AS discount_value,
       FORMAT(
           CASE
               WHEN v.discount_type = 1 THEN
                   CASE
                       WHEN b.total_amount * (v.price_reduced / 100) > v.prices_max THEN b.total_amount - v.prices_max + b.shipping_price
                       ELSE b.total_amount - (b.total_amount * (v.price_reduced / 100)) + b.shipping_price
                   END
               WHEN v.discount_type = 2 THEN b.total_amount - v.price_reduced + b.shipping_price
               ELSE b.total_amount + b.shipping_price
           END, 'N2') + ' VNĐ' AS total_after_discount      \s
       FROM bill b
       LEFT JOIN customer c ON c.id = b.id_customer
       LEFT JOIN voucher v ON v.id = b.id_voucher
       WHERE b.id = :idBill
""", nativeQuery = true)
    List<Object[]> getBillByIdCreatePDF(@Param("idBill") Integer idBill);


    @Query(value = """
        select
        	p.name_product +
            N'-- Màu sắc: ' + cl.name_color +
            N'-- Kích cỡ: ' + s.name_size AS product_info,
                bd.quantity,
                            FORMAT(bd.price_root, 'N2') + ' VNĐ' AS price_root,
                            FORMAT(bd.price, 'N2') + ' VNĐ' AS price,
                            FORMAT(bd.total_amount, 'N2') + ' VNĐ' AS total_amount
        from bill_detail bd
        left join product_detail pd
        on pd.id = bd.id_product_detail
        join product p
        on pd.id_product = p.id
        join color cl on cl.id = pd.id_color
        join size s on s.id = pd.id_size
        where bd.id_bill = :idCheck
""", nativeQuery = true)
    List<Object[]> getBillDetailByIdBillPDF(@Param("idCheck") Integer id);
    @Query(value = """
       SELECT
           b.id,
           b.code_bill,
            CASE
             WHEN b.address != N'Không có' THEN
                 LEFT(b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) + 1) - 1)
             ELSE
                 CASE
                     WHEN b.id_customer IS NOT NULL THEN CAST(c.full_name AS NVARCHAR)
                     ELSE N'khách lẻ'
                 END
         END AS name_customer,
           b.price_discount AS discount_amount,
           CASE
               WHEN b.total_amount > 0 THEN (b.price_discount / b.total_amount) * 100
               ELSE 0
           END AS discount_ratio_percentage,
           SUM(bd.quantity) AS total_products
       FROM bill b
       LEFT JOIN voucher v ON v.id = b.id_voucher
       LEFT JOIN bill_detail bd ON bd.id_bill = b.id
       LEFT JOIN customer c ON c.id = b.id_customer
       WHERE b.id = :idBill
       GROUP BY
           b.id,
           b.code_bill,
           b.id_customer,
           c.full_name,
           b.address,
           b.total_amount,
           b.price_discount;
""",nativeQuery = true)
    List<Object[]> getInfomationBillReturn(@Param("idBill") Integer id);
    @Query(value = """
    select
    	e.id,
    	b.id,
    	e.code_return_bill_exchange_bill,
    	b.code_bill,
    	 CASE
           WHEN b.address != N'Không có' THEN
               TRIM(SUBSTRING(b.address, 1, CHARINDEX(',', b.address) - 1))
           ELSE
               CASE
                   WHEN b.id_customer IS NOT NULL THEN CAST(c.full_name AS NVARCHAR)
                   ELSE N'khách lẻ'
               END
        END AS name_customer,
           -- đã sửa: Tương tự kiểm tra vị trí dấu phẩy tiếp theo, nếu không có trả về 'Không có'
          CASE
           WHEN b.address != N'Không có' THEN
                   TRIM(SUBSTRING(b.address, CHARINDEX(',', b.address) + 1, CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) - CHARINDEX(',', b.address) - 1))
           ELSE
               CASE
                   WHEN b.id_customer IS NOT NULL THEN CAST(c.number_phone AS NVARCHAR)
                   ELSE N'Không có'
               END
        END AS number_phone_customer,
           CASE
           WHEN b.address != N'Không có' THEN
               TRIM(SUBSTRING(b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) + 1, CHARINDEX(',', b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) + 1) - CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) - 1))
           ELSE
               CASE
                   WHEN b.id_customer IS NOT NULL THEN CAST(c.email AS NVARCHAR)
                   ELSE N'Không có'
               END
        END AS email_customer,
        CASE
            WHEN b.address != N'Không có' THEN
               TRIM(SUBSTRING(b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) + 1) + 1) + 1) + 1) + 1, LEN(b.address)))
            ELSE
                CASE
                    WHEN b.id_customer IS NOT NULL THEN CAST(TRIM(SUBSTRING(c.address, CHARINDEX(',', c.address, CHARINDEX(',', c.address, CHARINDEX(',', c.address) + 1) + 1) + 1, LEN(c.address))) AS NVARCHAR)
                    ELSE N'Không có'
                END
        END AS add_ress,
    	e.customer_refund,
    	e.reason,
    	e.update_date,
    	e.status,
    	e.customer_payment,
    	e.exchange_and_return_fee,
    	e.discounted_amount
    from return_bill_exchange_bill e
    left join bill b on b.id = e.id_bill
    left join customer c on b.id_customer = c.id
    where e.id = :idCheck and e.status = 1
""",nativeQuery = true)
    List<Object[]> getInformationPDF_Return_Exchange_Bill(@Param("idCheck") Integer idCheck);
    @Query(value = """
    SELECT
        p.name_product +
        N'-- Màu sắc: ' + cl.name_color +
        N'-- Kích cỡ: ' + s.name_size AS product_info,
        rb.quantity_return,
        FORMAT(bd.price, 'N2') + ' VNĐ' AS price_root,
        FORMAT(rb.price_buy, 'N2') + ' VNĐ' AS price_return,
        FORMAT(rb.total_return, 'N2') + ' VNĐ' AS total_amount
    FROM
    return_bill_detail rb
        LEFT JOIN return_bill_exchange_bill rbr ON rbr.id = rb.id_return_bill
        LEFT JOIN bill b ON rbr.id_bill = b.id
        LEFT JOIN bill_detail bd ON bd.id_bill = b.id
        JOIN product_detail pd ON pd.id = rb.id_product_detail
        LEFT JOIN product p ON pd.id_product = p.id
        LEFT JOIN color cl ON cl.id = pd.id_color
        LEFT JOIN size s ON s.id = pd.id_size
    WHERE
    rb.id_return_bill = :idReturnBillCheck
""",nativeQuery = true)
    List<Object[]> getListProductReturn(@Param("idReturnBillCheck") Integer idReturnBillCheck);
    @Query(value = """

    SELECT
        p.name_product +
        N'-- Màu sắc: ' + cl.name_color +
        N'-- Kích cỡ: ' + s.name_size AS product_info,
        eb.quantity_exchange,
        FORMAT(CAST(eb.price_root_at_time AS DECIMAL(18, 2)), 'N2') + ' VNĐ' AS price_root,
        FORMAT(CAST(eb.price_at_the_time_of_exchange AS DECIMAL(18, 2)), 'N2') + ' VNĐ' AS price_exchange,
        FORMAT(CAST(eb.total_exchange AS DECIMAL(18, 2)), 'N2') + ' VNĐ' AS total_amount_exchange
    FROM
    exchange_bill_detail eb
        LEFT JOIN return_bill_exchange_bill rbr ON rbr.id = eb.id_exchang_bill
        LEFT JOIN bill b ON rbr.id_bill = b.id
        LEFT JOIN bill_detail bd ON bd.id_bill = b.id  
        JOIN product_detail pd ON pd.id = eb.id_product_detail
        LEFT JOIN product p ON pd.id_product = p.id
        LEFT JOIN color cl ON cl.id = pd.id_color
        LEFT JOIN size s ON s.id = pd.id_size
    WHERE eb.id_exchang_bill = :idExchangeBillCheck
""",nativeQuery = true)
    List<Object[]> getListProductExchange(@Param("idExchangeBillCheck") Integer idReturnBillCheck);
    @Query("select pd from ProductDetail  pd where pd.id = :idCheck")
    ProductDetail getProductDteailById(@Param("idCheck") Integer id);

    @Query("""
    select b from Bill b where b.codeBill = :codeBillCheck
""")
    Bill getBillByCodeBill(
            @Param("codeBillCheck") String codeBillCheck
    );
}
