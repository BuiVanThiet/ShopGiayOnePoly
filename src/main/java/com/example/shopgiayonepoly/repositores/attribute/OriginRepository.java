package com.example.shopgiayonepoly.repositores.attribute;

import com.example.shopgiayonepoly.entites.Origin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OriginRepository extends JpaRepository<Origin, Integer> {
    @Query("select client from Origin client where client.status <> 0")
    List<Origin> getClientNotStatus0();
}
