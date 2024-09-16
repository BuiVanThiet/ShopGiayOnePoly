package com.example.shopgiayonepoly.service.attribute;

import com.example.shopgiayonepoly.entites.Color;
import com.example.shopgiayonepoly.entites.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ColorSevice {
    List<Color> findAll();

    <S extends Color> S save(S entity);

    Optional<Color> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    List<Color> findAll(Sort sort);

    Page<Color> findAll(Pageable pageable);


}
