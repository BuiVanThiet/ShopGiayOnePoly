package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.History;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface HistoryService {
    <S extends History> List<S> findAll(Example<S> example);

    <S extends History> List<S> saveAll(Iterable<S> entities);

    List<History> findAll();

    List<History> findAllById(Iterable<Integer> integers);

    <S extends History> S save(S entity);

    Optional<History> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    Page<History> findAll(Pageable pageable);
}
