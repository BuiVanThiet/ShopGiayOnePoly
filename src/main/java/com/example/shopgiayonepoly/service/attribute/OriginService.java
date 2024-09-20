package com.example.shopgiayonepoly.service.attribute;

import com.example.shopgiayonepoly.entites.Origin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface OriginService {
    List<Origin> findAll();

    <S extends Origin> S save(S entity);

    Optional<Origin> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    List<Origin> findAll(Sort sort);

    Page<Origin> findAll(Pageable pageable);

    List<Origin> getClientNotStatus0();
}
