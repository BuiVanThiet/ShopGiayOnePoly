package com.example.shopgiayonepoly.repositores.attribute;

import com.example.shopgiayonepoly.entites.Color;
import com.example.shopgiayonepoly.entites.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {
    @Query("select manufacturer from Manufacturer manufacturer where manufacturer.status <> 0")
    List<Manufacturer> getManufacturerNotStatus0();

    @Query("select manufacturer from Manufacturer manufacturer where manufacturer.status = 0")
    List<Manufacturer> getManufacturerDelete();
}
