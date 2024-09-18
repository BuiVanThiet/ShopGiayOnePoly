package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.response.BillTotalInfornationResponse;
import com.example.shopgiayonepoly.dto.response.ClientBillInformationResponse;
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
    select new com.example.shopgiayonepoly.dto.response.BillTotalInfornationResponse(
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
            "and concat(v.nameVoucher+v.codeVoucher) LIKE %:keyword%")
    Page<Voucher> getVoucherByBill(@Param("idBill") Integer idBill, @Param("keyword") String keyword, Pageable pageable);


    @Query("select new com.example.shopgiayonepoly.dto.response.ClientBillInformationResponse(addRess.customer.fullName,addRess.customer.numberPhone,addRess.specificAddress,addRess.specificAddress,addRess.specificAddress,addRess.specificAddress) from AddressShip addRess where addRess.customer.id = :idClient")
    List<ClientBillInformationResponse> getClientBillInformationResponse(@Param("idClient") Integer idBill);

 }
