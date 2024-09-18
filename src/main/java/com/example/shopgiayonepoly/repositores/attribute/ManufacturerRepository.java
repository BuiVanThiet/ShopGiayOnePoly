package com.example.shopgiayonepoly.repositores.attribute;

import com.example.shopgiayonepoly.entites.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {
    @Query("select client from Manufacturer client where client.status <> 0")
    List<Manufacturer> getClientNotStatus0();
}
