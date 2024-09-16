package com.example.shopgiayonepoly.repositores.attribute;

import com.example.shopgiayonepoly.entites.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {

}
