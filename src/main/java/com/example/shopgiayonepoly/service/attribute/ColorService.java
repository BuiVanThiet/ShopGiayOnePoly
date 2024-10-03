package com.example.shopgiayonepoly.service.attribute;

import com.example.shopgiayonepoly.entites.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ColorService {
    List<Color> findAll();

    <S extends Color> S save(S entity);

    Optional<Color> findById(Integer integer);

    long count();

    void deleteByID(int id);

    List<Color> findAll(Sort sort);

    Page<Color> findAll(Pageable pageable);

    List<Color> getColorNotStatus0();

    List<Color> getColorDelete();

    void updateStatus(int id, int status);

    void updateColor(int id, String codeColor, String nameColor);

}
