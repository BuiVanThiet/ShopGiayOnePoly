package com.example.shopgiayonepoly.service.attribute;

import com.example.shopgiayonepoly.entites.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface SizeService {
    List<Size> findAll();

    <S extends Size> S save(S entity);

    Optional<Size> findById(Integer integer);

    long count();

    void deleteByID(int id);

    List<Size> findAll(Sort sort);

    Page<Size> findAll(Pageable pageable);

    List<Size> getSizeNotStatus0();

    List<Size> getSizeDelete();

    void updateStatus(int id, int status);

    void updateSize(int id, String codeSize, String nameSize);

}
