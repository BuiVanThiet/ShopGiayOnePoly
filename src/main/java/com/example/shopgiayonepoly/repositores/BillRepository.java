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
            "WHERE (SELECT b.totalAmount FROM Bill b WHERE b.id = :idBill) >= v.pricesApply " +
            "AND v.status <> 0 " +
            "AND (v.id <> (SELECT b.voucher.id FROM Bill b WHERE b.id = :idBill) OR (SELECT b.voucher.id FROM Bill b WHERE b.id = :idBill) IS NULL) " +
            "and concat(v.nameVoucher,v.codeVoucher) LIKE %:keyword%")
    Page<Voucher> getVoucherByBill(@Param("idBill") Integer idBill, @Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT v FROM Voucher v " +
            "WHERE (SELECT b.totalAmount FROM Bill b WHERE b.id = :idBill) >= v.pricesApply " +
            "AND v.status <> 0 " +
            "AND (v.id <> (SELECT b.voucher.id FROM Bill b WHERE b.id = :idBill) OR (SELECT b.voucher.id FROM Bill b WHERE b.id = :idBill) IS NULL) " +
            "and concat(v.nameVoucher,v.codeVoucher) LIKE %:keyword%")
    List<Voucher> getVoucherByBill(@Param("idBill") Integer idBill, @Param("keyword") String keyword);

    @Query("select new com.example.shopgiayonepoly.dto.response.bill.ClientBillInformationResponse(cuss.fullName,cuss.numberPhone,cuss.email,cuss.addRess,cuss.addRess,cuss.addRess,cuss.addRess) from Customer cuss where cuss.id = :idClient")
    List<ClientBillInformationResponse> getClientBillInformationResponse(@Param("idClient") Integer idBill);
    // phan nay danh cho quan ly hoa don
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
            case 
            when voucher.discountType = 1 then
                case 
                    when bill.totalAmount * (voucher.priceReduced / 100) > voucher.pricesMax then
                        bill.totalAmount - voucher.pricesMax + bill.shippingPrice
                    else 
                        bill.totalAmount - (bill.totalAmount * (voucher.priceReduced / 100)) + bill.shippingPrice
                end 
            when voucher.discountType = 2 then
                bill.totalAmount - voucher.priceReduced + bill.shippingPrice
            else 
                bill.totalAmount + bill.shippingPrice
        end, 
            bill.paymentMethod, 
            bill.billType, 
            bill.paymentStatus, 
            bill.surplusMoney, 
            bill.createDate, 
            bill.updateDate,
            bill.status) 
        from Bill bill 
        LEFT JOIN bill.customer customer
        LEFT JOIN bill.staff staff
        LEFT JOIN bill.voucher voucher
        where 
        bill.status <> 0 and
        (concat(COALESCE(bill.codeBill, ''), COALESCE(bill.customer.fullName, ''), COALESCE(bill.customer.numberPhone, '')) like %:nameCheck%)
         and (:statusCheck is null or (bill.status = :statusCheck))
    """)
    Page<BillResponseManage> getAllBillByStatusDiss0(@Param("nameCheck") String nameCheck, @Param("statusCheck") Integer statusCheck, Pageable pageable);

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
         and (:statusCheck is null or (bill.status = :statusCheck))
    """)
    List<BillResponseManage> getAllBillByStatusDiss0(@Param("nameCheck") String nameCheck, @Param("statusCheck") Integer statusCheck);

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
        end
     )
     from Bill b
     left join b.voucher v
     where b.id = :idCheck
""")
    InformationBillByIdBillResponse getInformationBillByIdBill(@Param("idCheck") Integer idBill);
}
