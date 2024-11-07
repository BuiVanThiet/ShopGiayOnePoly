package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.response.bill.BillResponseManage;
import com.example.shopgiayonepoly.dto.response.bill.BillTotalInfornationResponse;
import com.example.shopgiayonepoly.dto.response.bill.ClientBillInformationResponse;
import com.example.shopgiayonepoly.dto.response.bill.InformationBillByIdBillResponse;
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
    @Query("select client from Customer client where client.status <> 0")
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
            "AND CONCAT(v.nameVoucher, v.codeVoucher) LIKE %:keyword% " +
            "ORDER BY v.priceReduced DESC")
    Page<Voucher> getVoucherByBill(@Param("idBillCheck") Integer idBillCheck, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT v FROM Voucher v " +
            "WHERE (SELECT b.totalAmount FROM Bill b WHERE b.id = :idBillCheck) >= v.pricesApply " +
            "AND v.status <> 0 " +
            "AND (v.id <> (SELECT b.voucher.id FROM Bill b WHERE b.id = :idBillCheck) OR (SELECT b.voucher.id FROM Bill b WHERE b.id = :idBillCheck) IS NULL) " +
            "AND CONCAT(v.nameVoucher, v.codeVoucher) LIKE %:keyword% " +
            "ORDER BY v.priceReduced DESC")
    List<Voucher> getVoucherByBill(@Param("idBillCheck") Integer idBillCheck, @Param("keyword") String keyword);

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
            bill.codeBill, 
            bill.customer, 
            bill.staff, 
            bill.addRess, 
            bill.voucher, 
            bill.shippingPrice, 
            bill.cash, 
            bill.acountMoney, 
            bill.note, 
            bill.totalAmount - bill.priceDiscount, 
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
        	(b.cash+b.surplus_money) AS so_tien,
        	N'Tiền mặt'  payment_method,
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
        where b.id = :idCheck and invo.status = 101  AND b.cash > 0)
        UNION ALL
        (select
        	b.id,
        	b.update_date,
        	b.acount_money AS so_tien,
        	N'Tiền tài khoản'  payment_method,
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
""", nativeQuery = true)
    List<Object[]> getInfoPaymentByIdBill(@Param("idCheck") Integer idCheck);

    @Query(value = """
    SELECT
 b.code_bill,
    b.create_date,
    -- đã sửa: Kiểm tra nếu tìm thấy dấu phẩy thì mới sử dụng SUBSTRING, nếu không trả về 'Không có'
    CASE
        WHEN CHARINDEX(',', b.address) > 0 THEN SUBSTRING(b.address, 1, CHARINDEX(',', b.address) - 1)
        ELSE 'Không có'
    END AS full_name,
   
    -- đã sửa: Tương tự kiểm tra vị trí dấu phẩy tiếp theo, nếu không có trả về 'Không có'
    CASE
        WHEN CHARINDEX(',', b.address) > 0 AND CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) > 0 THEN
            SUBSTRING(b.address, CHARINDEX(',', b.address) + 1, CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) - CHARINDEX(',', b.address) - 1)
        ELSE 'Không có'
    END AS number_phone,
   
    COALESCE(c.email, 'Không có') AS email,
   
    -- đã sửa: Kiểm tra và sử dụng SUBSTRING khi có đủ dấu phẩy, nếu không trả về 'Không có'
    CASE
        WHEN CHARINDEX(',', c.addRess, CHARINDEX(',', c.addRess, CHARINDEX(',', c.addRess) + 1) + 1) > 0 THEN
            SUBSTRING(c.addRess, CHARINDEX(',', c.addRess, CHARINDEX(',', c.addRess, CHARINDEX(',', c.addRess) + 1) + 1) + 2, LEN(c.addRess))
        ELSE 'Không có'
    END AS addRess,
   
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
        END, 'N2') + ' VNĐ' AS total_after_discount       
    FROM bill b
    LEFT JOIN customer c ON c.id = b.id_customer
    LEFT JOIN voucher v ON v.id = b.id_voucher
    WHERE b.id = :idBill
""", nativeQuery = true)
    List<Object[]> getBillByIdCreatePDF(@Param("idBill") Integer idBill);


    @Query(value = """
        select
        	 p.name_product,
                bd.quantity,
                            FORMAT(bd.price_root, 'N2') + ' VNĐ' AS price_root,
                            FORMAT(bd.price, 'N2') + ' VNĐ' AS price,
                            FORMAT(bd.total_amount, 'N2') + ' VNĐ' AS total_amount
        from bill_detail bd
        left join product_detail pd
        on pd.id = bd.id_product_detail
        join product p
        on pd.id_product = p.id
        where bd.id_bill = :idCheck
""", nativeQuery = true)
    List<Object[]> getBillDetailByIdBillPDF(@Param("idCheck") Integer id);
    @Query(value = """
       SELECT
           b.id,
           b.code_bill,
           CASE
               WHEN b.id_customer IS NULL THEN N'Không có'
               WHEN CHARINDEX(',', b.address) = 0 THEN b.address
               ELSE LEFT(b.address, CHARINDEX(',', b.address) - 1)
           END AS customer_status,
           b.price_discount AS discount_amount,
           CASE
               WHEN b.total_amount > 0 THEN (b.price_discount / b.total_amount) * 100
               ELSE 0
           END AS discount_ratio_percentage,
           SUM(bd.quantity) AS total_products
       FROM bill b
       LEFT JOIN voucher v ON v.id = b.id_voucher
       LEFT JOIN bill_detail bd ON bd.id_bill = b.id
       WHERE b.id = :idBill
       GROUP BY
           b.id,
           b.code_bill,
           b.id_customer,
           b.address,
           b.total_amount,
           b.price_discount;
""",nativeQuery = true)
    List<Object[]> getInfomationBillReturn(@Param("idBill") Integer id);

    @Query("select pd from ProductDetail  pd where pd.id = :idCheck")
    ProductDetail getProductDteailById(@Param("idCheck") Integer id);
}
