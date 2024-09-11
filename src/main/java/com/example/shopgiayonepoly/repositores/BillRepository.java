package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Bill;
import com.example.shopgiayonepoly.entites.Client;
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
    @Query("select client from Client client where client.status <> 0")
    List<Client> getClientNotStatus0();
}
