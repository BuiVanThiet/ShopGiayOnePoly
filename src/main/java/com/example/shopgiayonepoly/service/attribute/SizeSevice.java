package com.example.shopgiayonepoly.service.attribute;

import com.example.shopgiayonepoly.entites.Manufacturer;
import com.example.shopgiayonepoly.entites.Origin;
import com.example.shopgiayonepoly.entites.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface SizeSevice {
    List<Size> findAll();

    <S extends Size> S save(S entity);

    Optional<Size> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    List<Size> findAll(Sort sort);

    Page<Size> findAll(Pageable pageable);


}
