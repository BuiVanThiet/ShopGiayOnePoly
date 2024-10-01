package com.example.shopgiayonepoly.repositores.attribute;

import com.example.shopgiayonepoly.entites.Origin;
import com.example.shopgiayonepoly.entites.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OriginRepository extends JpaRepository<Origin, Integer> {
    @Query("select origin from Origin origin where origin.status <> 0")
    List<Origin> getOriginNotStatus0();

    @Query("select origin from Origin origin where origin.status = 0")
    List<Origin> getOriginDelete();
}
