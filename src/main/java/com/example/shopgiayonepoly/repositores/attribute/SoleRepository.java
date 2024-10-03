package com.example.shopgiayonepoly.repositores.attribute;

import com.example.shopgiayonepoly.entites.Sole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoleRepository extends JpaRepository<Sole, Integer> {
    @Query("select sole from Sole sole where sole.status <> 0")
    List<Sole> getSoleNotStatus0();

    @Query("select sole from Sole sole where sole.status = 0")
    List<Sole> getSoleDelete();
}
