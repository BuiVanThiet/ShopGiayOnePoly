package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.response.BillTotalInfornationResponse;
import com.example.shopgiayonepoly.entites.Bill;
import com.example.shopgiayonepoly.entites.Client;
import com.example.shopgiayonepoly.entites.Voucher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill,Integer> {
    //ngay 3thang9
    @Query("select b from Bill b where b.status = 0 order by b.createDate desc")
    List<Bill> getBillByStatusNew(Pageable pageable);
    @Query("select client from Client client where client.status <> 0")
    List<Client> getClientNotStatus0();

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
        end
    )
    from Bill b 
    left join b.voucher v 
    where b.id = :billId
""")
    BillTotalInfornationResponse findBillVoucherById(@Param("billId") Integer billId);

    @Query("select v from Voucher v")
    List<Voucher> getVouCherByBill();

 }
