package com.example.shopgiayonepoly.service.attribute;

import com.example.shopgiayonepoly.entites.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface MaterialSevice {
    List<Material> findAll();

    <S extends Material> S save(S entity);

    Optional<Material> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    List<Material> findAll(Sort sort);

    Page<Material> findAll(Pageable pageable);

    List<Material> getClientNotStatus0();

}
