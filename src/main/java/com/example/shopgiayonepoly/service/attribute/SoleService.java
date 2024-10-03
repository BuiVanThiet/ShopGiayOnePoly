package com.example.shopgiayonepoly.service.attribute;

import com.example.shopgiayonepoly.entites.Sole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface SoleService {
    List<Sole> findAll();

    <S extends Sole> S save(S entity);

    Optional<Sole> findById(Integer integer);

    long count();

    void deleteByID(int id);

    List<Sole> findAll(Sort sort);

    Page<Sole> findAll(Pageable pageable);

    List<Sole> getSoleNotStatus0();

    List<Sole> getSoleDelete();

    void updateStatus(int id, int status);

    void updateSole(int id, String codeSole, String nameSole);

}
