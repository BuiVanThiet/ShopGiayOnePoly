package com.example.shopgiayonepoly.service.attribute;

import com.example.shopgiayonepoly.entites.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ManufacturerService {
    List<Manufacturer> findAll();

    <S extends Manufacturer> S save(S entity);

    Optional<Manufacturer> findById(Integer integer);

    long count();

    void deleteByID(int id);

    List<Manufacturer> findAll(Sort sort);

    Page<Manufacturer> findAll(Pageable pageable);

    List<Manufacturer> getManufacturerNotStatus0();

    List<Manufacturer> getManufacturerDelete();

    void updateStatus(int id, int status);

    void updateManufacturer(int id, String codeManufacturer, String nameManufacturer);

}
