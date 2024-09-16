package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher,Integer> {
}
