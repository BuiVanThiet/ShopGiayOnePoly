package com.example.shopgiayonepoly.service.attribute;

import com.example.shopgiayonepoly.entites.Manufacturer;
import com.example.shopgiayonepoly.entites.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ManufacturerSevice {
    List<Manufacturer> findAll();

    <S extends Manufacturer> S save(S entity);

    Optional<Manufacturer> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    List<Manufacturer> findAll(Sort sort);

    Page<Manufacturer> findAll(Pageable pageable);


}
