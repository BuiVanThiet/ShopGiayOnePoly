package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.entites.History;
import com.example.shopgiayonepoly.repositores.HistoryReponsitory;
import com.example.shopgiayonepoly.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryImplement implements HistoryService {
    @Autowired
    HistoryReponsitory historyReponsitory;

    @Override
    public <S extends History> List<S> findAll(Example<S> example) {
        return historyReponsitory.findAll(example);
    }

    @Override
    public <S extends History> List<S> saveAll(Iterable<S> entities) {
        return historyReponsitory.saveAll(entities);
    }

    @Override
    public List<History> findAll() {
        return historyReponsitory.findAll();
    }

    @Override
    public List<History> findAllById(Iterable<Integer> integers) {
        return historyReponsitory.findAllById(integers);
    }

    @Override
    public <S extends History> S save(S entity) {
        return historyReponsitory.save(entity);
    }

    @Override
    public Optional<History> findById(Integer integer) {
        return historyReponsitory.findById(integer);
    }

    @Override
    public long count() {
        return historyReponsitory.count();
    }

    @Override
    public void deleteById(Integer integer) {
        historyReponsitory.deleteById(integer);
    }

    @Override
    public Page<History> findAll(Pageable pageable) {
        return historyReponsitory.findAll(pageable);
    }
}
