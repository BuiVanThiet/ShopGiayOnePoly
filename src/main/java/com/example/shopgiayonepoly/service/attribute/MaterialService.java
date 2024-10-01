package com.example.shopgiayonepoly.service.attribute;

import com.example.shopgiayonepoly.entites.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface MaterialService {
    List<Material> findAll();

    <S extends Material> S save(S entity);

    Optional<Material> findById(Integer integer);

    long count();

    void deleteByID(int id);

    List<Material> findAll(Sort sort);

    Page<Material> findAll(Pageable pageable);

    List<Material> getClientNotStatus0();

    List<Material> getMaterialDelete();

    void updateStatus(int id, int status);

    void updateMaterial(int id, String codeMaterial, String nameMaterial);

}
