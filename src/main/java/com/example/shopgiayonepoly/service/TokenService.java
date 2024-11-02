package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.Token;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TokenService {
    List<Token> findAll();

    void save(Token entity);

    Optional<Token> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    void delete(Token entity);

    Page<Token> findAll(Pageable pageable);

    Token saveGetId(Token entity);

    <S extends Token> S saveOrUpdate(S entity);
}
