package com.example.shopgiayonepoly.repositores.attribute;

import com.example.shopgiayonepoly.entites.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {
    @Query("select client from Color client where client.status <> 0")
    List<Color> getClientNotStatus0();

    @Query("select color from Color color where color.status = 0")
    List<Color> getColorDelete();
}
