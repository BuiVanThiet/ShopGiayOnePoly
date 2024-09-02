package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill,Integer> {
}
