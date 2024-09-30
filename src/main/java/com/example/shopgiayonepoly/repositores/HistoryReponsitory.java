package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.History;
import com.example.shopgiayonepoly.entites.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryReponsitory extends JpaRepository<History, Integer> {
}
